package com.bigbade.skriptbot.command;

import com.bigbade.skriptbot.BasicCommandTestSetup;
import com.bigbade.skriptbot.URLConnectionHandler;
import com.bigbade.skriptbot.commands.PastebinCommand;
import com.bigbade.skriptbot.testutils.TestAttachment;
import com.bigbade.skriptbot.testutils.TestMessage;
import com.bigbade.skriptbot.testutils.TestUser;
import com.bigbade.skriptbot.utils.MessageUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PastebinCommandTest extends BasicCommandTestSetup<PastebinCommand> {
    private static final String TEST_PASTE_VALUE = "test paste";
    private static final String ENCODED_POST_REQUEST = "api_option=paste&api_paste_private=1&" +
            "api_paste_name=Test_User%27s+File%3A+test.txt&api_paste_expire_date=1D&" +
            "api_paste_format=txt&api_paste_code=test+paste&api_dev_key=key";

    private final TestMessage testUploadMessage = new TestMessage("", getTestChannel());
    private final TestMessage completeTestMessage = new TestMessage(".paste fakeid", getTestChannel());

    @BeforeEach
    void setupEach() {
        testUploadMessage.setAuthor(new TestUser("Test_User"));
        testUploadMessage.addAttachment(new TestAttachment("test.txt", 1,
                new ByteArrayInputStream(TEST_PASTE_VALUE.getBytes(StandardCharsets.UTF_8))));
    }

    @Order(1)
    @Test
    void testCommandErrors() {
        TestMessage commandMessage = new TestMessage(".paste", getTestChannel());
        getTestChannel().expectMessage(new TestMessage(MessageUtils.getErrorMessage(commandMessage.getId(), "Invalid Arguments",
                "Usage: **.pastebin (message id)**\nTo get a Message ID, enable" +
                        "developer mode in Discord settings, right click the message, and click \"Copy ID\""), getTestChannel()));
        sendMessage(commandMessage);
        verify();

        getTestChannel().expectMessage(new TestMessage(MessageUtils.getErrorMessage(completeTestMessage.getId(), "No Message Found",
                "There was no message in this channel with that id."), getTestChannel()));
        sendMessage(completeTestMessage);
        verify();

        getTestChannel().setRetrievedMessage(new TestMessage("", getTestChannel()));

        getTestChannel().expectMessage(new TestMessage(MessageUtils.getErrorMessage(completeTestMessage.getId(), "No File Found",
                "There was no attached file to that message."), getTestChannel()));
        sendMessage(completeTestMessage);
        verify();
    }

    @Order(2)
    @Test
    void testPastebinError() {
        getTestChannel().setRetrievedMessage(testUploadMessage);

        TestMessage outputMessage = new TestMessage(MessageUtils.getErrorMessage(completeTestMessage.getId(), "Error Creating Paste",
                "Error: Bad API Response Test"), getTestChannel());
        URLConnectionHandler.resetValues("Bad API Response Test", ENCODED_POST_REQUEST, null);
        getTestChannel().expectMessage(outputMessage);

        sendMessage(completeTestMessage);

        outputMessage.verify(false);
        verify();
    }

    @Order(3)
    @Test
    void testUploadPastebin() {
        URLConnectionHandler.resetValues("https://pastebin/paste", ENCODED_POST_REQUEST, null);
        TestMessage outputMessage = new TestMessage(new EmbedBuilder().setTitle("Pastebin'd file")
                .setDescription("https://pastebin/paste").setFooter(completeTestMessage.getId()).build(), getTestChannel());
        getTestChannel().expectMessage(outputMessage);
        testUploadMessage.addAttachment(new TestAttachment("test.txt", 1,
                new ByteArrayInputStream(TEST_PASTE_VALUE.getBytes(StandardCharsets.UTF_8))));
        getTestChannel().setRetrievedMessage(testUploadMessage);

        sendMessage(completeTestMessage);

        outputMessage.verify(false);
        verify();
    }

    @Override
    public PastebinCommand getTestedCommand() {
        return new PastebinCommand("key", ".");
    }
}
