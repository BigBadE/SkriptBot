package software.bigbade.skriptbot.command;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.Jsoner;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.bigbade.skriptbot.commands.DocSearchCommand;
import software.bigbade.skriptbot.testutils.TestChannel;
import software.bigbade.skriptbot.testutils.TestMessage;
import software.bigbade.skriptbot.testutils.TestUser;
import software.bigbade.skriptbot.utils.MessageUtils;
import software.bigbade.skriptbot.utils.ResourceDataFetcher;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DocSearchCommandTest {
    private static final TestResourceDataFetcher TEST_DATA_FETCHER = new TestResourceDataFetcher();
    private static final DocSearchCommand DOC_SEARCH_COMMAND = new DocSearchCommand(TEST_DATA_FETCHER, ".");

    private static final TestChannel TEST_TEXT_CHANNEL = new TestChannel();
    private static final User TEST_USER = new TestUser();
    private static final MessageReaction.ReactionEmote MOCKED_REACTION_EMOTE = mock(MessageReaction.ReactionEmote.class);

    private static final MessageEmbed TEST_EMBED = new EmbedBuilder().setTitle("Test Docs",
            "https://docs.skunity.com/syntax/search/Test+Docs")
            .addField("Pattern", "```test[s]```", false)
            .addField("Description", "Test type", false)
            .addField("Example", "```unit tests > no unit tests```", false)
            .addField("Addon", "Skript", true)
            .addField("Requires", "Skript", true)
            .build();

    private static final MessageEmbed TEST_MULTI_EMBED = new EmbedBuilder()
            .setTitle("Found Results", "https://docs.skunity.com/syntax/search/test+3")
            .addField("0. Test Docs (type)", "```test[s]```", false)
            .addField("1. Second Docs (type)", "```second type[s]```", false)
            .build();

    private static JsonArray TEST_ARRAY;

    @SneakyThrows(JsonException.class)
    @BeforeAll
    static void setupTestArray() {
        TEST_ARRAY = (JsonArray) Jsoner.deserialize("[{\"name\": \"Test Docs\",\"doc\": \"types\"," +
                "\"desc\": \"Test type\",\"addon\": \"Skript\",\"version\": \"1.0\",\"pattern\": \"test[s]\"," +
                "\"plugin\": \"\",\"examples\": [{\"example\": \"unit tests &lt; no unit tests\"}]},{\"name\": " +
                "\"Second Docs\",\"doc\": \"types\",\"desc\": \"Second type\",\"addon\": \"Skript\",\"version\": " +
                "\"1.0\",\"pattern\": \"second type[s]\",\"plugin\": \"\",\"examples\": [{\"example\": \"second\"}]}]");

        TEST_DATA_FETCHER.addResult("test 1", new JsonArray());

        JsonArray newArray = new JsonArray();
        newArray.add(TEST_ARRAY.get(0));
        TEST_DATA_FETCHER.addResult("test 2", newArray);

        TEST_DATA_FETCHER.addResult("test 3", TEST_ARRAY);
    }

    @Test
    void testGetNumberFromString() {
        Assertions.assertEquals(10, DocSearchCommand.getNumberFromString("10."));
        Assertions.assertEquals(400, DocSearchCommand.getNumberFromString("400."));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void testGetNumberFromEmote() {
        Assertions.assertEquals("U+37U+fe0fU+20e3", DocSearchCommand.getNumberEmote(7));
        Assertions.assertThrows(IllegalArgumentException.class, () -> DocSearchCommand.getNumberEmote(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> DocSearchCommand.getNumberEmote(11));
    }

    @Test
    void testCommandErrorMessages() {
        TEST_TEXT_CHANNEL.expectMessage(new TestMessage(MessageUtils.getErrorMessage("No Syntax Specified",
                "Usage: **.doc subtext**"), TEST_TEXT_CHANNEL));
        DOC_SEARCH_COMMAND.onCommand(TEST_TEXT_CHANNEL, new String[0]);
        TEST_TEXT_CHANNEL.verify();

        TEST_TEXT_CHANNEL.expectMessage(new TestMessage(MessageUtils.getErrorMessage("No Results",
                "No results were found for that query"), TEST_TEXT_CHANNEL));
        DOC_SEARCH_COMMAND.onCommand(TEST_TEXT_CHANNEL, new String[]{"test", "1"});
        TEST_TEXT_CHANNEL.verify();
    }

    @Test
    void testCommandBasicDocs() {
        TEST_TEXT_CHANNEL.expectMessage(new TestMessage(TEST_EMBED, TEST_TEXT_CHANNEL));
        DOC_SEARCH_COMMAND.onCommand(TEST_TEXT_CHANNEL, new String[]{"test", "2"});
        TEST_TEXT_CHANNEL.verify();

        TEST_TEXT_CHANNEL.expectMessage(new TestMessage(TEST_MULTI_EMBED, TEST_TEXT_CHANNEL));
        DOC_SEARCH_COMMAND.onCommand(TEST_TEXT_CHANNEL, new String[]{"test", "3"});
        TEST_TEXT_CHANNEL.verify();
    }

    @Test
    void testDocsReaction() {
        EmbedBuilder builder = new EmbedBuilder();
        DOC_SEARCH_COMMAND.addDocsResponse(builder, TEST_ARRAY, 0);

        TestMessage testMessage = new TestMessage("", TEST_TEXT_CHANNEL);
        TestMessage commandMessage = new TestMessage("", TEST_TEXT_CHANNEL);
        DOC_SEARCH_COMMAND.onReaction(TEST_USER, commandMessage, testMessage, MOCKED_REACTION_EMOTE);
        testMessage.verify(false);
        commandMessage.verify(false);
        testMessage = new TestMessage(builder.build(), TEST_TEXT_CHANNEL);
        commandMessage = new TestMessage(".d test 3", TEST_TEXT_CHANNEL);
        when(MOCKED_REACTION_EMOTE.isEmoji()).thenReturn(true);

        when(MOCKED_REACTION_EMOTE.getAsCodepoints()).thenReturn(DocSearchCommand.getNumberEmote(0));

        TEST_TEXT_CHANNEL.expectMessage(new TestMessage(TEST_EMBED, TEST_TEXT_CHANNEL));

        DOC_SEARCH_COMMAND.onReaction(TEST_USER, commandMessage, testMessage, MOCKED_REACTION_EMOTE);
        testMessage.verify(true);
    }
}

class TestResourceDataFetcher extends ResourceDataFetcher {
    private final Map<String, JsonArray> results = new HashMap<>();

    public TestResourceDataFetcher() {
        super("testKey");
    }

    public void addResult(String query, JsonArray result) {
        results.put(query, result);
    }

    @Override
    public JsonArray getDocsResults(String query) {
        return results.get(query);
    }
}
