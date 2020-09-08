package software.bigbade.skriptbot.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.Color;

public final class MessageUtils {
    public static final String DELETE_REACTION = "U+274C";
    private MessageUtils() {}

    public static void sendEmbedWithReaction(TextChannel channel, MessageEmbed message) {
        channel.sendMessage(message).queue(response -> response.addReaction(DELETE_REACTION).queue());
    }

    public static MessageEmbed getErrorMessage(String title, String description) {
        return new EmbedBuilder().setTitle(title)
                .setColor(Color.RED)
                .setFooter("Error")
                .setThumbnail("https://i.imgur.com/AjlWaz5.png")
                .setDescription(description).build();
    }
}
