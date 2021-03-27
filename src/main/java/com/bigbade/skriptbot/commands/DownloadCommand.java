package com.bigbade.skriptbot.commands;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import com.bigbade.skriptbot.api.ICommand;
import com.bigbade.skriptbot.api.IDataFetcher;
import com.bigbade.skriptbot.utils.JsonKeys;
import com.bigbade.skriptbot.utils.MessageUtils;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class DownloadCommand implements ICommand {
    @Getter
    private final List<String> aliases = Collections.unmodifiableList(Arrays.asList("down", "download", "downloads"));
    @Getter
    private EmbedBuilder embed;
    private long nextVersionCheck = 0L;

    @Nonnull
    private final IDataFetcher dataFetcher;

    @Override
    public void onCommand(Message message, String[] args) {
        if (nextVersionCheck < System.currentTimeMillis()) {
            embed = updateEmbedMessage();
            nextVersionCheck = System.currentTimeMillis() + 14400000L;
        }
        MessageUtils.sendEmbedWithReaction(message.getTextChannel(), new EmbedBuilder(embed)
                .setFooter("Downloads | " + message.getId()).build());
    }

    public EmbedBuilder updateEmbedMessage() {
        JsonObject latestReturned = (JsonObject)
                dataFetcher.readData("https://api.github.com/repos/SkriptLang/Skript/releases/latest")
                        .orElseThrow(() -> new IllegalStateException("Could not fetch latest Skript release!"));
        String latestVersion = latestReturned.getString(JsonKeys.TAG_NAME.getKey());
        JsonObject matoReturned = (JsonObject)
                dataFetcher.readData("https://api.github.com/repos/Matocolotoe/Skript-1.8/releases/latest")
                        .orElseThrow(() -> new IllegalStateException("Could not fetch latest Skript release!"));
        String matoVersion = matoReturned.getString(JsonKeys.TAG_NAME.getKey());
        return new EmbedBuilder().setTitle("Downloading Skript")
                .setColor(Color.GREEN).addField("1.7", "Recommended but **not supported**:\n\n[Njol, 2.2-SNAPSHOT]" +
                        "(https://github.com/Pikachu920/Skript/releases/download/2.2-SNAPSHOT/Skript-2.2-Njol.jar)", true)
                .addField("1.8", "Recommended but **not supported**:\n\n"
                        + "[Matocolotoe fork, " + matoVersion + "](" + getJarDownload(matoReturned) + ")", true)
                .addField("1.9+", "Long Term Support: \n\n"
                        + "[Bensku Fork 2.4.1](https://github.com/SkriptLang/Skript/releases/download/2.4.1/Skript.jar)\n\n"
                        + "Latest: \n\n"
                        + "[Bensku's fork (" + latestVersion + ")](" + getJarDownload(latestReturned) + "), "
                        + "but it might be unstable", true)
                .addField("Extra Info", "Note that all versions of Skript not by Njol are unofficial and " +
                        "their issues should be reported to their respective issue trackers", false)
                .setFooter("Downloads | ");
    }

    private static String getJarDownload(JsonObject latestJson) {
        for (Object object : latestJson.<JsonArray>getCollection(JsonKeys.ASSETS.getKey())) {
            JsonObject asset = (JsonObject) object;
            if (asset.getString(JsonKeys.CONTENT_TYPE.getKey()).equals("application/java-archive")) {
                return asset.getString(JsonKeys.URL.getKey());
            }
        }
        throw new IllegalStateException("No jar assets in release!");
    }
}
