package com.bigbade.skriptbot.commands;

import com.bigbade.skriptbot.api.ICommand;
import com.bigbade.skriptbot.api.IDataFetcher;
import com.bigbade.skriptbot.utils.JsonKeys;
import com.bigbade.skriptbot.utils.MessageUtils;
import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class DownloadCommand implements ICommand {
    @Getter
    private final List<String> aliases = Collections.unmodifiableList(Arrays.asList("dl", "down", "download", "downloads"));
    @Nonnull
    private final IDataFetcher dataFetcher;

    @Getter
    private EmbedBuilder embed;
    private long nextVersionCheck = 0L;

    @Override
    public void onCommand(Message message, String[] args) {
        if (embed == null || nextVersionCheck < System.currentTimeMillis()) {
            updateEmbedMessage();
        }
        MessageUtils.sendEmbedWithReaction(message.getTextChannel(), new EmbedBuilder(embed)
                .setFooter("Downloads | " + message.getId()).build());
    }

    public EmbedBuilder updateEmbedMessage() {
        JsonObject latestReturned = (JsonObject)
                dataFetcher.readData("https://api.github.com/repos/SkriptLang/Skript/releases/latest")
                        .orElseThrow(() -> new IllegalStateException("Could not fetch latest Skript release!"));
        String latestVersion = latestReturned.getString(JsonKeys.TAG_NAME.getKey());
        JsonObject latestUnstableReturned = (JsonObject)
                dataFetcher.<JsonArray>readData("https://api.github.com/repos/SkriptLang/Skript/releases")
                        .orElseThrow(() -> new IllegalStateException("Could not fetch latest Skript release!")).get(0);
        String latestUnstableVersion = latestReturned.getString(JsonKeys.TAG_NAME.getKey());
        JsonObject matoReturned = (JsonObject)
                dataFetcher.readData("https://api.github.com/repos/Matocolotoe/Skript-1.8/releases/latest")
                        .orElseThrow(() -> new IllegalStateException("Could not fetch latest Skript release!"));
        String matoVersion = matoReturned.getString(JsonKeys.TAG_NAME.getKey());
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(Color.GREEN).setTitle("Downloading Skript")
                .addField("1.7", "Recommended but **not supported**:\n\n[Njol, 2.2-SNAPSHOT]" +
                        "(https://github.com/Pikachu920/Skript/releases/download/2.2-SNAPSHOT/Skript-2.2-Njol.jar)", true)
                .addField("1.8", "Recommended but **not supported**:\n\n"
                        + "[Matocolotoe fork, " + matoVersion + "]("
                        + matoReturned.getString(JsonKeys.HTML_URL.getKey()) + ")", true)
                .addField("1.9+", "Latest Stable: \n\n"
                        + "[Bensku's fork (" + latestVersion + ")]("
                        + latestReturned.getString(JsonKeys.HTML_URL.getKey()) + ")", true)
                .setFooter("Downloads | ");
        if (!latestUnstableVersion.equals(latestVersion)) {
            builder.addField("Unstable", "Latest Unstable: \n\n"
                    + "[Bensku's fork (" + latestUnstableVersion + ")]("
                    + latestUnstableReturned.getString(JsonKeys.HTML_URL.getKey()) + "), but it might be unstable", true);
        }
        builder.addField("Extra Info", "Note that all versions of Skript not by Njol are unofficial and " +
                "their issues should be reported to their respective issue trackers", false);
        nextVersionCheck = System.currentTimeMillis() + 14400000L;
        embed = builder;
        return builder;
    }
}
