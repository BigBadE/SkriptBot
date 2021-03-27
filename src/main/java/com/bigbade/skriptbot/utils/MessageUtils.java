package com.bigbade.skriptbot.utils;

import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.Color;

public final class MessageUtils {
    public static final String DELETE_REACTION = "U+274C";
    private MessageUtils() {}

    public static void sendEmbedWithReaction(TextChannel channel, MessageEmbed message) {
        channel.sendMessage(message).queue(response -> response.addReaction(DELETE_REACTION).queue());
    }

    public static MessageEmbed getErrorMessage(String id, String title, String description) {
        return new MessageEmbed(null, title, description, EmbedType.RICH, null, Color.RED.getRGB(),
                new MessageEmbed.Thumbnail("https://i.imgur.com/AjlWaz5.png", null, 0, 0), null, null, null,
                new MessageEmbed.Footer("Error | " + id, null, null), null, null);
    }
}
