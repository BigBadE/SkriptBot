package software.bigbade.skriptbot.command;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.Jsoner;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.mockito.internal.verification.VerificationModeFactory;
import software.bigbade.skriptbot.commands.DocSearchCommand;
import software.bigbade.skriptbot.utils.MessageUtils;
import software.bigbade.skriptbot.utils.ResourceDataFetcher;

import java.util.Collections;
import java.util.function.Consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DocSearchCommandTest {
    private static final Message MOCK_MESSAGE = mock(Message.class);
    private static final Message MOCK_COMMAND_MESSAGE = mock(Message.class);

    private static final ResourceDataFetcher MOCK_DATA_FETCHER = mock(ResourceDataFetcher.class);
    private static final DocSearchCommand DOC_SEARCH_COMMAND = new DocSearchCommand(MOCK_DATA_FETCHER, ".");
    private static final TextChannel MOCK_TEXT_CHANNEL = mock(TextChannel.class);
    private static final User MOCK_USER = mock(User.class);
    private static final MessageReaction.ReactionEmote MOCKED_REACTION_EMOTE = mock(MessageReaction.ReactionEmote.class);
    private static final MockedStatic<MessageUtils> MOCKED_MESSAGE_UTILS = mockStatic(MessageUtils.class);
    @SuppressWarnings("unchecked")
    private static final AuditableRestAction<Void> MOCK_MESSAGE_DELETE = mock(AuditableRestAction.class);
    private static RestAction<Void> mockReactionDelete;
    private static MessageAction mockMessageAction;

    private static final MessageEmbed TEST_EMBED = new EmbedBuilder().setTitle("Test Docs",
            "https://docs.skunity.com/syntax/search/Test+Docs")
            .addField("Pattern", "```test[s]```", false)
            .addField("Description", "Test type", false)
            .addField("Example", "```unit tests > no unit tests```", false)
            .addField("Addon", "Skript", true)
            .addField("Requires", "Skript", true)
            .build();

    private static final MessageEmbed TEST_MULTI_EMBED = new EmbedBuilder()
            .setTitle("Found Results", "https://docs.skunity.com/syntax/search/test+docs")
            .addField("0. Test Docs (type)", "```test[s]```", false)
            .addField("1. Second Docs (type)", "```second type[s]```", false)
            .build();

    private static JsonArray TEST_ARRAY;

    @SneakyThrows
    @BeforeAll
    static void setupTestArray() {
        TEST_ARRAY = (JsonArray) Jsoner.deserialize("[{\"name\": \"Test Docs\",\"doc\": \"types\"," +
                "\"desc\": \"Test type\",\"addon\": \"Skript\",\"version\": \"1.0\",\"pattern\": \"test[s]\"," +
                "\"plugin\": \"\",\"examples\": [{\"example\": \"unit tests &lt; no unit tests\"}]},{\"name\": " +
                "\"Second Docs\",\"doc\": \"types\",\"desc\": \"Second type\",\"addon\": \"Skript\",\"version\": " +
                "\"1.0\",\"pattern\": \"second type[s]\",\"plugin\": \"\",\"examples\": [{\"example\": \"second\"}]}]");
    }

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setupMessageAction() {
        mockMessageAction = mock(MessageAction.class);
        mockReactionDelete = mock(RestAction.class);
    }

    @AfterAll
    static void closeMockedStatic() {
        MOCKED_MESSAGE_UTILS.close();
    }

    @AfterEach
    void resetMockedStatic() {
        MOCKED_MESSAGE_UTILS.reset();
    }

    @Test
    void testGetNumberFromString() {
        Assertions.assertEquals(10, DocSearchCommand.getNumberFromString("10."));
        Assertions.assertEquals(400, DocSearchCommand.getNumberFromString("400."));
    }

    @Test
    void testGetNumberFromEmote() {
        Assertions.assertEquals("U+37U+fe0fU+20e3", DocSearchCommand.getNumberEmote(7));
    }

    @Test
    void testCommandErrorMessages() {
        MOCKED_MESSAGE_UTILS.when(() -> MessageUtils
                .getErrorMessage(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenCallRealMethod();
        DOC_SEARCH_COMMAND.onCommand(MOCK_TEXT_CHANNEL, new String[0]);
        MOCKED_MESSAGE_UTILS.verify(() -> MessageUtils.sendEmbedWithReaction(MOCK_TEXT_CHANNEL,
                MessageUtils.getErrorMessage("No Syntax Specified",
                        "Usage: **.doc subtext**")));
        when(MOCK_DATA_FETCHER.getDocsResults("test docs")).thenReturn(new JsonArray());
        MOCKED_MESSAGE_UTILS.reset();
        DOC_SEARCH_COMMAND.onCommand(MOCK_TEXT_CHANNEL, new String[] { "test", "docs" });
        MOCKED_MESSAGE_UTILS.verify(() -> MessageUtils.sendEmbedWithReaction(MOCK_TEXT_CHANNEL,
                MessageUtils.getErrorMessage("No Results",
                        "No results were found for that query")));
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    @Test
    void testCommandBasicDocs() {
        ArgumentCaptor<MessageEmbed> captor = ArgumentCaptor.forClass(MessageEmbed.class);
        when(MOCK_TEXT_CHANNEL.sendMessage(captor.capture())).thenReturn(mockMessageAction);
        JsonArray newArray = new JsonArray();
        newArray.add(TEST_ARRAY.get(0));
        when(MOCK_DATA_FETCHER.getDocsResults("test docs")).thenReturn(newArray);
        DOC_SEARCH_COMMAND.onCommand(MOCK_TEXT_CHANNEL, new String[] { "test", "docs" });
        verify(mockMessageAction).queue();
        assertEmbedsEqual(TEST_EMBED, captor.getValue());
        when(MOCK_DATA_FETCHER.getDocsResults("test docs")).thenReturn(TEST_ARRAY);
        when(MOCK_MESSAGE.addReaction(ArgumentMatchers.anyString())).thenReturn(mockReactionDelete);
        DOC_SEARCH_COMMAND.onCommand(MOCK_TEXT_CHANNEL, new String[] { "test", "docs" });
        ArgumentCaptor<Consumer<Message>> argumentCaptor = ArgumentCaptor.forClass(Consumer.class);
        verify(mockMessageAction).queue(argumentCaptor.capture());
        assertEmbedsEqual(TEST_MULTI_EMBED, captor.getValue());
        argumentCaptor.getValue().accept(MOCK_MESSAGE);
        verify(mockReactionDelete, VerificationModeFactory.times(5)).queue();
    }

    @Test
    void testDocsReaction() {
        EmbedBuilder builder = new EmbedBuilder();
        DOC_SEARCH_COMMAND.addDocsResponse(builder, TEST_ARRAY, 0);
        DOC_SEARCH_COMMAND.onReaction(MOCK_USER, MOCK_COMMAND_MESSAGE, MOCK_MESSAGE, MOCKED_REACTION_EMOTE);
        when(MOCK_MESSAGE.getEmbeds()).thenReturn(Collections.singletonList(builder.build()));
        when(MOCKED_REACTION_EMOTE.isEmoji()).thenReturn(true);
        when(MOCK_COMMAND_MESSAGE.getContentDisplay()).thenReturn(".d test docs");
        when(MOCKED_REACTION_EMOTE.getAsCodepoints()).thenReturn(DocSearchCommand.getNumberEmote(0));
        when(MOCK_DATA_FETCHER.getDocsResults("test docs")).thenReturn(TEST_ARRAY);
        when(MOCK_MESSAGE.getChannel()).thenReturn(MOCK_TEXT_CHANNEL);
        ArgumentCaptor<MessageEmbed> captor = ArgumentCaptor.forClass(MessageEmbed.class);
        when(MOCK_TEXT_CHANNEL.sendMessage(captor.capture())).thenReturn(mockMessageAction);
        when(MOCK_MESSAGE.delete()).thenReturn(MOCK_MESSAGE_DELETE);
        DOC_SEARCH_COMMAND.onReaction(MOCK_USER, MOCK_COMMAND_MESSAGE, MOCK_MESSAGE, MOCKED_REACTION_EMOTE);

        verify(MOCK_MESSAGE_DELETE).queue();
        assertEmbedsEqual(TEST_EMBED, captor.getValue());
        verify(mockMessageAction).queue();
    }

    public static void assertEmbedsEqual(MessageEmbed expected, MessageEmbed actual) {
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getUrl(), actual.getUrl());
        for(int i = 0; i < expected.getFields().size(); i++) {
            Assertions.assertEquals(expected.getFields().get(i), actual.getFields().get(i));
        }
    }
}
