package com.bigbade.skriptbot.command;

import com.bigbade.skriptbot.BasicCommandTestSetup;
import com.bigbade.skriptbot.DocsPage;
import com.bigbade.skriptbot.commands.DocSearchCommand;
import com.bigbade.skriptbot.testutils.TestJDA;
import com.bigbade.skriptbot.testutils.TestMessage;
import com.bigbade.skriptbot.testutils.TestResourceDataFetcher;
import com.bigbade.skriptbot.testutils.TestUser;
import com.bigbade.skriptbot.utils.MessageUtils;
import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.util.Arrays;

class DocSearchCommandTest extends BasicCommandTestSetup<DocSearchCommand> {
    private static TestResourceDataFetcher TEST_DATA_FETCHER = new TestResourceDataFetcher();

    private static TestUser TEST_USER = new TestUser("Test User");

    private static MessageReaction.ReactionEmote REACTION_EMOTE = MessageReaction.ReactionEmote.fromUnicode(
            TestMessage.hexToName(DocsPage.getNumberEmote(0)), TestJDA.TEST_JDA);

    private static MessageEmbed TEST_EMBED = new EmbedBuilder().setTitle("Test Docs",
            "https://docs.skunity.com/syntax/search/Test+Docs")
            .setColor(Color.YELLOW)
            .addField("Pattern", "```vb\ntest[s]```", false)
            .addField("Description", "Test type", false)
            .addField("Example", "```vb\nunit tests > no unit tests```", false)
            .addField("Addon", "Skript", true)
            .addField("Requires", "Skript", true)
            .build();

    private static EmbedBuilder TEST_MULTI_EMBED = new EmbedBuilder()
            .setTitle("Found Results", "https://docs.skunity.com/syntax/search/test+3")
            .addField("0. Test Docs (type)", "```vb\ntest[s]```", false)
            .addField("1. Second Docs (type)", "```vb\nsecond type[s]```", false);

    private static final JsonObject FIRST_OBJECT = getObject("{\"name\": \"Test Docs\"," +
            "\"doc\": \"types\"," +
            "\"desc\": \"Test type\"," +
            "\"addon\": \"Skript\"," +
            "\"version\": \"1.0\"," +
            "\"pattern\": \"test[s]\"," +
            "\"plugin\": \"\"," +
            "\"examples\": [{" +
            "\"example\": \"unit tests &lt; no unit tests\"}]}");

    private static final JsonObject SECOND_OBJECT = getObject("{\"name\": \"Second Docs\"," +
            "\"doc\": \"types\"," +
            "\"desc\": \"Second type\"," +
            "\"addon\": \"Skript\"," +
            "\"version\": \"1.0\"," +
            "\"pattern\": \"second type[s]\"," +
            "\"plugin\": \"\"," +
            "\"examples\": [{" +
            "\"example\": \"second\"}]}");

    private static final JsonArray TEST_ARRAY = new JsonArray(Arrays.asList(FIRST_OBJECT, SECOND_OBJECT));

    @SneakyThrows(JsonException.class)
    private static JsonObject getObject(String json) {
        return (JsonObject) Jsoner.deserialize(json);
    }

    @BeforeAll
    static void setupTestArray() {
        TEST_DATA_FETCHER.addResult("test 1", new JsonArray());

        JsonArray newArray = new JsonArray();
        newArray.add(FIRST_OBJECT);
        TEST_DATA_FETCHER.addResult("test 2", newArray);

        TEST_DATA_FETCHER.addResult("test 3", TEST_ARRAY);
    }

    @AfterAll
    static void afterAll() {
        TEST_DATA_FETCHER = null;
        TEST_USER = null;
        REACTION_EMOTE = null;
        TEST_EMBED = null;
        TEST_MULTI_EMBED = null;
    }

    @Test
    void testGetNumberFromString() {
        Assertions.assertEquals(10, DocSearchCommand.getNumberFromString("10."));
        Assertions.assertEquals(400, DocSearchCommand.getNumberFromString("400."));
    }

    @Test
    void testCommandErrorMessages() {
        TestMessage testMessage = new TestMessage(".d", getTestChannel());
        getTestChannel().expectMessage(new TestMessage(MessageUtils.getErrorMessage(testMessage.getId(), "No Syntax Specified",
                "Usage: **.doc subtext**"), getTestChannel()));
        sendMessage(testMessage);
        verify();

        testMessage = new TestMessage(".d test 1", getTestChannel());
        getTestChannel().expectMessage(new TestMessage(MessageUtils.getErrorMessage(testMessage.getId(), "No Results",
                "No results were found for that query"), getTestChannel()));
        sendMessage(testMessage);
        verify();
    }

    @Test
    void testCommandBasicDocs() {
        TestMessage testMessage = new TestMessage(".d test 2", getTestChannel());
        getTestChannel().expectMessage(new TestMessage(new EmbedBuilder(TEST_EMBED)
                .setFooter(DocSearchCommand.FOOTER + testMessage.getId()).build(), getTestChannel()));
        sendMessage(testMessage);
        verify();

        testMessage = new TestMessage(".d test 3", getTestChannel());
        getTestChannel().expectMessage(new TestMessage(new EmbedBuilder(TEST_MULTI_EMBED)
                .setFooter(DocSearchCommand.FOOTER + testMessage.getId()).build(), getTestChannel()));
        sendMessage(testMessage);
        verify();
    }

    @Test
    void testDocsReaction() {
        EmbedBuilder builder = new EmbedBuilder().setTitle("Found Results");
        getCommand().addDocsResponse(builder, TEST_ARRAY, 0);

        TestMessage testMessage = new TestMessage(builder.build(), getTestChannel());
        TestMessage commandMessage = new TestMessage(".d test 2", getTestChannel());

        testMessage.verify(false);
        commandMessage.verify(false);
        testMessage = new TestMessage(builder.build(), getTestChannel());
        commandMessage = new TestMessage(".d test 3", getTestChannel());

        getTestChannel().expectMessage(new TestMessage(
                TEST_EMBED,
                getTestChannel()));

        addReaction(commandMessage, testMessage, TEST_USER, REACTION_EMOTE);
        verify();
        testMessage.verify(false);
    }

    @Override
    public DocSearchCommand getTestedCommand() {
        return new DocSearchCommand(TEST_DATA_FETCHER, ".");
    }
}
