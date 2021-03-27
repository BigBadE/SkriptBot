package com.bigbade.skriptbot.utils;

import com.bigbade.skriptbot.testutils.TestChannel;
import com.bigbade.skriptbot.testutils.TestIDHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.Color;

class MessageUtilsTest {
    private final MessageEmbed testEmbed = new EmbedBuilder().setTitle("Testing!").setDescription("It works!").build();
    private final TestChannel testChannel = new TestChannel();

    @Test
    void testSendEmbedWithReaction() {
        testChannel.expectMessage(testEmbed);
        MessageUtils.sendEmbedWithReaction(testChannel, testEmbed);
        testChannel.verify();
    }

    @Test
    void testGetErrorMessage() {
        String id = TestIDHandler.getId() + "";
        MessageEmbed embed = MessageUtils.getErrorMessage(id, "title", "description");
        Assertions.assertEquals("title", embed.getTitle());
        Assertions.assertEquals("description", embed.getDescription());
        Assertions.assertEquals(Color.RED, embed.getColor());
        Assertions.assertNotNull(embed.getThumbnail());
        Assertions.assertEquals("https://i.imgur.com/AjlWaz5.png", embed.getThumbnail().getUrl());
        Assertions.assertNotNull(embed.getFooter());
        Assertions.assertEquals("Error | " + id, embed.getFooter().getText());
    }
}
