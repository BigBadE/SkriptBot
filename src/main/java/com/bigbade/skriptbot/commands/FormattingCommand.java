package com.bigbade.skriptbot.commands;

import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import com.bigbade.skriptbot.api.ICommand;
import com.bigbade.skriptbot.utils.MessageUtils;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FormattingCommand implements ICommand {
    public static final EmbedBuilder FORMATTING_EMBED = new EmbedBuilder().setTitle("Formatting Code In Discord")
            .setColor(Color.GREEN.getRGB()).addField(new MessageEmbed.Field("Why?",
                    "Code blocks make it easier for helpers to identify potential errors -- " +
                            "help them help you!", false))
            .addField(new MessageEmbed.Field("The Format", "\\`\\`\\`vb\n" +
                    "on chat:\n" +
                    "broadcast \"This is how you format code!\"" +
                    "\\`\\`\\`", true))
            .addField(new MessageEmbed.Field("How It Looks", "```vb\n" +
                    "on chat:\n" +
                    "broadcast \"This is how you format code!\"\n" +
                    "```", true))
            .addField(new MessageEmbed.Field("Extra Info", "On US keyboards, the grave character (`) is " +
                    "located above the tab key on the top left of the keyboard", false));
    @Getter
    private final List<String> aliases = Collections.unmodifiableList(Arrays.asList("format", "formatting"));

    @Override
    public void onCommand(Message message, String[] args) {
        MessageUtils.sendEmbedWithReaction(message.getTextChannel(), new EmbedBuilder(FORMATTING_EMBED)
                .setFooter("Format | " + message.getId()).build());
    }
}
