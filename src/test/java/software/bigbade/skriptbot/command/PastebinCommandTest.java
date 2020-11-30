package software.bigbade.skriptbot.command;

import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.mockito.internal.verification.VerificationModeFactory;
import org.slf4j.Logger;
import software.bigbade.skriptbot.URLConnectionHandler;
import software.bigbade.skriptbot.commands.PastebinCommand;
import software.bigbade.skriptbot.utils.MessageUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PastebinCommandTest {
    private static final PastebinCommand PASTEBIN_COMMAND = new PastebinCommand("key", ".");

    private static final TextChannel MOCK_TEXT_CHANNEL = mock(TextChannel.class);
    @SuppressWarnings("unchecked")
    private static final RestAction<Message> MOCK_GET_MESSAGE = mock(RestAction.class);
    private static final User MOCKED_USER = mock(User.class);
    private static final Message MOCK_FOUND_MESSAGE = mock(Message.class);
    private static final Message.Attachment MOCK_FOUND_ATTACHMENT = mock(Message.Attachment.class);
    @SuppressWarnings("unchecked")
    private static final CompletableFuture<InputStream> MOCK_ATTACHMENT_STREAM = mock(CompletableFuture.class);

    private static final MockedStatic<MessageUtils> MOCKED_MESSAGE_UTILS = mockStatic(MessageUtils.class);


    private static final String[] COMMAND_ARGS = new String[] { "fakeid" };

    @SuppressWarnings("unchecked")
    private static final ArgumentCaptor<Consumer<InputStream>> INPUT_STREAM_CAPTOR = ArgumentCaptor.forClass(Consumer.class);
    @SuppressWarnings("unchecked")
    private static final ArgumentCaptor<Consumer<Message>> MESSAGE_CAPTOR = ArgumentCaptor.forClass(Consumer.class);

    private static final String TEST_PASTE_VALUE = "test paste";
    private static final String ENCODED_POST_REQUEST = "api_option=paste&api_paste_private=1" +
            "&api_paste_name=Test%27s+File%3A+test.txt&api_paste_expire_date=1D" +
            "&api_paste_format=.txt&api_paste_code=test+paste&api_dev_key=key";

    @BeforeAll
    static void setupTests() {
        MOCKED_MESSAGE_UTILS.when(() -> MessageUtils
                .getErrorMessage(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenCallRealMethod();
        when(MOCK_TEXT_CHANNEL.retrieveMessageById(ArgumentMatchers.anyString())).thenReturn(MOCK_GET_MESSAGE);
        when(MOCK_GET_MESSAGE.onErrorMap(ArgumentMatchers.any())).thenReturn(MOCK_GET_MESSAGE);
        when(MOCK_FOUND_MESSAGE.getAttachments()).thenReturn(new ArrayList<>());
        when(MOCK_FOUND_ATTACHMENT.retrieveInputStream()).thenReturn(MOCK_ATTACHMENT_STREAM);
        when(MOCK_FOUND_MESSAGE.getAuthor()).thenReturn(MOCKED_USER);
        when(MOCKED_USER.getName()).thenReturn("Test");
        when(MOCK_FOUND_ATTACHMENT.getFileName()).thenReturn("test.txt");
        when(MOCK_FOUND_ATTACHMENT.getFileExtension()).thenReturn(".txt");
        when(MOCK_ATTACHMENT_STREAM.thenAccept(INPUT_STREAM_CAPTOR.capture())).thenAnswer(input -> {
            input.<Consumer<InputStream>>getArgument(0)
                    .accept(new ByteArrayInputStream(TEST_PASTE_VALUE.getBytes(StandardCharsets.UTF_8)));
            return null;
        });

        mockStatic(JDALogger.class).when(() -> JDALogger.getLog(ArgumentMatchers.any(Class.class)))
                .thenReturn(mock(Logger.class));
    }

    @AfterAll
    static void closeMockedStatic() {
        MOCKED_MESSAGE_UTILS.close();
    }

    @Order(1)
    @Test
    void testCommandErrors() {
        PASTEBIN_COMMAND.onCommand(MOCK_TEXT_CHANNEL, new String[0]);
        MOCKED_MESSAGE_UTILS.verify(() -> MessageUtils.sendEmbedWithReaction(MOCK_TEXT_CHANNEL,
                MessageUtils.getErrorMessage("Invalid Arguments",
                        "Usage: **.pastebin (message id)**\nTo get a Message ID, enable" +
                                "developer mode in Discord settings, right click the message, and click \"Copy ID\"")));
        PASTEBIN_COMMAND.onCommand(MOCK_TEXT_CHANNEL, COMMAND_ARGS);
        verify(MOCK_GET_MESSAGE, VerificationModeFactory.atLeastOnce()).queue(MESSAGE_CAPTOR.capture());
        MESSAGE_CAPTOR.getValue().accept(null);
        MOCKED_MESSAGE_UTILS.verify(() -> MessageUtils.sendEmbedWithReaction(MOCK_TEXT_CHANNEL,
                MessageUtils.getErrorMessage("No Message Found",
                        "There was no message in this channel with that id.")));
        PASTEBIN_COMMAND.onCommand(MOCK_TEXT_CHANNEL, COMMAND_ARGS);
        verify(MOCK_GET_MESSAGE, VerificationModeFactory.atLeastOnce()).queue(MESSAGE_CAPTOR.capture());
        MESSAGE_CAPTOR.getValue().accept(MOCK_FOUND_MESSAGE);
        MOCKED_MESSAGE_UTILS.verify(() -> MessageUtils.sendEmbedWithReaction(MOCK_TEXT_CHANNEL,
                MessageUtils.getErrorMessage("No File Found",
                        "There was no attached file to that message.")));
    }

    @Order(2)
    @Test
    void testUploadPastebin() {
        when(MOCK_FOUND_MESSAGE.getAttachments()).thenReturn(Collections.singletonList(MOCK_FOUND_ATTACHMENT));
        URLConnectionHandler.resetValues("Bad API Response Test", ENCODED_POST_REQUEST, null);
        callCommandWithAttachment();
        MOCKED_MESSAGE_UTILS.verify(() -> MessageUtils.sendEmbedWithReaction(MOCK_TEXT_CHANNEL,
                MessageUtils.getErrorMessage("Error Creating Paste",
                        "Error: Bad API Response Test")));
        URLConnectionHandler.resetValues("https://pastebin/paste", ENCODED_POST_REQUEST, null);
        callCommandWithAttachment();
        MOCKED_MESSAGE_UTILS.verify(() -> MessageUtils.sendEmbedWithReaction(MOCK_TEXT_CHANNEL,
                new EmbedBuilder().setTitle("Pastebin'd file")
                        .setDescription("https://pastebin/paste").build()));
    }

    private void callCommandWithAttachment() {
        PASTEBIN_COMMAND.onCommand(MOCK_TEXT_CHANNEL, COMMAND_ARGS);
        verify(MOCK_GET_MESSAGE, VerificationModeFactory.atLeastOnce()).queue(MESSAGE_CAPTOR.capture());
        MESSAGE_CAPTOR.getValue().accept(MOCK_FOUND_MESSAGE);
    }
}
