package software.bigbade.skriptbot.commands;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import java.awt.Color;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import software.bigbade.skriptbot.api.ICommand;
import software.bigbade.skriptbot.api.IDataFetcher;
import software.bigbade.skriptbot.utils.JsonKeys;
import software.bigbade.skriptbot.utils.MessageUtils;

@RequiredArgsConstructor
public class AddonSearchCommand implements ICommand {
  @Getter
  private final String[] aliases = new String[] {"a", "addon", "addons"};
  private final IDataFetcher dataFetcher;
  private final String prefix;

  @Override
  public void onCommand(Member sender, TextChannel channel, String id,
                        String[] args) {
    if (args.length == 0) {
      MessageUtils.sendEmbedWithReaction(
          channel, MessageUtils.getErrorMessage(id, "No Addon Specified",
                                                "Usage: **" + prefix +
                                                    "a skript-mirror**"));
      return;
    }
    Optional<String> addon =
        dataFetcher.getAddon(String.join(" ", args).toLowerCase());
    if (!addon.isPresent()) {
      MessageUtils.sendEmbedWithReaction(
          channel,
          MessageUtils.getErrorMessage(
              id, "No Addon Found", "No addons were found with that search"));
      return;
    }
    JsonObject found =
        (JsonObject)dataFetcher
            .readData("https://api.skripttools.net/v4/addons/" + addon.get())
            .orElseThrow(()
                             -> new IllegalStateException(
                                 "Addon in list has invalid URL"));
    if (!found.getBoolean(JsonKeys.SUCCESS.getKey())) {
      throw new IllegalStateException("Error getting file: " +
                                      found.getString(JsonKeys.ERROR.getKey()));
    }
    JsonObject data = (JsonObject)found.get("data");
    List<String> authors = new ArrayList<>();
    for (Object element : (JsonArray)data.get("author")) {
      authors.add((String)element);
    }
    StringBuilder authorBuilder = new StringBuilder();
    for (int i = 0; i < authors.size(); i++) {
      String author = authors.get(i);
      if (i == authors.size() - 1) {
        authorBuilder.append(author);
      } else if (i == authors.size() - 2) {
        authorBuilder.append(author).append(", and ");
      } else {
        authorBuilder.append(author).append(", ");
      }
    }
    JsonObject dependencies = (JsonObject)data.get("depend");
    String softDependencies;
    if (dependencies.containsKey("softdepend")) {
      StringBuilder dependencyBuilder = new StringBuilder();
      for (Object dependency :
           (JsonArray)((JsonObject)data.get("depend")).get("softdepend")) {
        dependencyBuilder.append((String)dependency).append(", ");
      }
      softDependencies =
          dependencyBuilder.substring(0, dependencyBuilder.length() - 2);
    } else {
      softDependencies = "None";
    }

    MessageUtils.sendEmbedWithReaction(
        channel,
        new EmbedBuilder()
            .setTitle(data.getString(JsonKeys.PLUGIN.getKey()) + " " +
                          data.getString(JsonKeys.VERSION.getKey()),
                      data.getString(JsonKeys.DOWNLOAD.getKey()))
            .setColor(Color.GREEN)
            .addField("Addon",
                      "**" + addon.get() + "** by **" + authorBuilder + "**",
                      true)
            .addField("Description",
                      data.getString(JsonKeys.DESCRIPTION.getKey()), true)
            .addField("Soft Depends", softDependencies, true)
            .addField("Download (" +
                          humanReadableByteCountSI(
                              data.getLong(JsonKeys.BYTES.getKey())) +
                          ")",
                      data.getString(JsonKeys.DOWNLOAD.getKey()), false)
            .setFooter("Data provided by Duetro's Skript API")
            .build());
  }

  /**
   * From
   * https://programming.guide/java/formatting-byte-size-to-human-readable-format.html
   * Converts bytes to SI format, ex:
   * 1001 bytes to 1Kb
   * @param bytes Amount of bytes
   * @return Human readable format
   */
  public static String humanReadableByteCountSI(long bytes) {
    if (-1000 < bytes && bytes < 1000) {
      return bytes + " B";
    }
    CharacterIterator ci = new StringCharacterIterator("kMGTPE");
    while (bytes <= -999950 || bytes >= 999950) {
      bytes /= 1000;
      ci.next();
    }
    return String.format("%.1f %cB", bytes / 1000.0, ci.current());
  }
}
