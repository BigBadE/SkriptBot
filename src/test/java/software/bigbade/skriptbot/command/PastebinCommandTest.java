package software.bigbade.skriptbot.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentMatchers;
import org.slf4j.Logger;
import software.bigbade.skriptbot.URLConnectionHandler;
import software.bigbade.skriptbot.commands.PastebinCommand;
import software.bigbade.skriptbot.testutils.TestAttachment;
import software.bigbade.skriptbot.testutils.TestChannel;
import software.bigbade.skriptbot.testutils.TestIDHandler;
import software.bigbade.skriptbot.testutils.TestMessage;
import software.bigbade.skriptbot.testutils.TestUser;
import software.bigbade.skriptbot.utils.MessageUtils;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PastebinCommandTest {
    private static final PastebinCommand PASTEBIN_COMMAND = new PastebinCommand("key", ".");

    private static final TestChannel TEST_TEXT_CHANNEL = new TestChannel();
    private static final TestMessage TEST_UPLOAD_MESSAGE = new TestMessage("", TEST_TEXT_CHANNEL);

    private static final String[] COMMAND_ARGS = new String[]{"fakeid"};

    private static final String TEST_PASTE_VALUE = "test paste";
    private static final String ENCODED_POST_REQUEST = "api_option=paste&api_paste_private=1&api_paste_name=Test_User%27s+File%3A+test.txt&api_paste_expire_date=1D&api_paste_format=txt&api_paste_code=test+paste&api_dev_key=key";

    @BeforeAll
    static void setupLogger() {
        mockStatic(JDALogger.class).when(() -> JDALogger.getLog(ArgumentMatchers.any(Class.class)))
                .thenReturn(mock(Logger.class));

        TEST_UPLOAD_MESSAGE.setAuthor(new TestUser("Test_User"));
        TEST_UPLOAD_MESSAGE.addAttachment(new TestAttachment("test.txt", 1,
                new ByteArrayInputStream(TEST_PASTE_VALUE.getBytes(StandardCharsets.UTF_8))));
    }

    @Order(1)
    @Test
    void testCommandErrors() {
        String id = TestIDHandler.getId() + "";
        TEST_TEXT_CHANNEL.expectMessage(new TestMessage(MessageUtils.getErrorMessage(id, "Invalid Arguments",
                "Usage: **.pastebin (message id)**\nTo get a Message ID, enable" +
                        "developer mode in Discord settings, right click the message, and click \"Copy ID\""), TEST_TEXT_CHANNEL));
        PASTEBIN_COMMAND.onCommand(TEST_TEXT_CHANNEL, id, new String[0]);
        TEST_TEXT_CHANNEL.verify();

        id = TestIDHandler.getId() + "";
        TEST_TEXT_CHANNEL.expectMessage(new TestMessage(MessageUtils.getErrorMessage(id, "No Message Found",
                "There was no message in this channel with that id."), TEST_TEXT_CHANNEL));
        PASTEBIN_COMMAND.onCommand(TEST_TEXT_CHANNEL, id, COMMAND_ARGS);
        TEST_TEXT_CHANNEL.verify();

        TEST_TEXT_CHANNEL.setRetrievedMessage(new TestMessage("", TEST_TEXT_CHANNEL));

        id = TestIDHandler.getId() + "";
        TEST_TEXT_CHANNEL.expectMessage(new TestMessage(MessageUtils.getErrorMessage(id, "No File Found",
                "There was no attached file to that message."), TEST_TEXT_CHANNEL));
        PASTEBIN_COMMAND.onCommand(TEST_TEXT_CHANNEL, id, COMMAND_ARGS);
        TEST_TEXT_CHANNEL.verify();
    }

    @Order(2)
    @Test
    void testPastebinError() {
        TEST_TEXT_CHANNEL.setRetrievedMessage(TEST_UPLOAD_MESSAGE);

        String id = TestIDHandler.getId() + "";
        TestMessage outputMessage = new TestMessage(MessageUtils.getErrorMessage(id, "Error Creating Paste",
                "Error: Bad API Response Test"), TEST_TEXT_CHANNEL);
        URLConnectionHandler.resetValues("Bad API Response Test", ENCODED_POST_REQUEST, null);
        TEST_TEXT_CHANNEL.expectMessage(outputMessage);

        PASTEBIN_COMMAND.onCommand(TEST_TEXT_CHANNEL, id, COMMAND_ARGS);

        outputMessage.verify(false);
        TEST_TEXT_CHANNEL.verify();
    }

    @Order(3)
    @Test
    void testUploadPastebin() {
        String id = TestIDHandler.getId() + "";
        URLConnectionHandler.resetValues("https://pastebin/paste", ENCODED_POST_REQUEST, null);
        TestMessage outputMessage = new TestMessage(new EmbedBuilder().setTitle("Pastebin'd file")
                .setDescription("https://pastebin/paste").setFooter(id).build(), TEST_TEXT_CHANNEL);
        TEST_TEXT_CHANNEL.expectMessage(outputMessage);
        TEST_UPLOAD_MESSAGE.addAttachment(new TestAttachment("test.txt", 1,
                new ByteArrayInputStream(TEST_PASTE_VALUE.getBytes(StandardCharsets.UTF_8))));
        TEST_TEXT_CHANNEL.setRetrievedMessage(TEST_UPLOAD_MESSAGE);

        PASTEBIN_COMMAND.onCommand(TEST_TEXT_CHANNEL, id, COMMAND_ARGS);

        outputMessage.verify(false);
        TEST_TEXT_CHANNEL.verify();
    }
}
