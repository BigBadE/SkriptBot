package com.bigbade.skriptbot.command;

import com.bigbade.skriptbot.BasicCommandTestSetup;
import com.bigbade.skriptbot.commands.AddonSearchCommand;
import com.bigbade.skriptbot.testutils.TestMessage;
import com.bigbade.skriptbot.testutils.TestResourceDataFetcher;
import com.bigbade.skriptbot.utils.MessageUtils;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.Jsonable;
import com.github.cliftonlabs.json_simple.Jsoner;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.Color;

class AddonSearchCommandTest extends BasicCommandTestSetup<AddonSearchCommand> {
    private static TestResourceDataFetcher TEST_DATA_FETCHER = new TestResourceDataFetcher();

    private static final EmbedBuilder TEST_EMBED = new EmbedBuilder()
            .setTitle("SkTest 1.0.0", "https://www.youtube.com/watch?v=dQw4w9WgXcQ").setColor(Color.GREEN.getRGB())
            .addField("Addon", "**test-addon** by **Test, Testing, and BigBadE**", true)
            .addField("Description", "Testing.", true)
            .addField("Soft Depends", "Skript", true)
            .addField(new MessageEmbed.Field("Download (1.0 kB)", "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                    false));

    private static final String TEST_JSON = "{\"success\": true,\"data\": {\"author\": [\"Test\", \"Testing\", \"BigBadE\"" +
            "],\"description\": \"Testing.\",\"plugin\": \"SkTest\",\"version\": \"1.0.0\",\"bytes\": \"1000\"," +
            "\"download\": \"https://www.youtube.com/watch?v=dQw4w9WgXcQ\",\"depend\": {\"softdepend\": [\"Skript\"]}}}";

    public AddonSearchCommand getTestedCommand() {
        return new AddonSearchCommand(TEST_DATA_FETCHER, ".");
    }

    @AfterAll
    static void afterAll() {
        TEST_DATA_FETCHER = null;
    }

    @Test
    void testShortByteCount() {
        Assertions.assertEquals("12 B", AddonSearchCommand.shortByteCount(12));
        Assertions.assertEquals("1.0 kB", AddonSearchCommand.shortByteCount(1000));
        Assertions.assertEquals("1.9 kB", AddonSearchCommand.shortByteCount(1949));
        Assertions.assertEquals("1.0 MB", AddonSearchCommand.shortByteCount(1000000));
        Assertions.assertEquals("1.9 GB", AddonSearchCommand.shortByteCount(1940000000));
    }

    @SneakyThrows(JsonException.class)
    @Test
    void testEmbedErrors() {
        TestMessage sending = new TestMessage(".a", getTestChannel());
        getTestChannel().expectMessage(new TestMessage(
                MessageUtils.getErrorMessage(sending.getId(), "No Addon Specified", "Usage: **.a skript-mirror**"),
                getTestChannel()));
        sendMessage(sending);
        verify();

        TEST_DATA_FETCHER.addAddon("test addon", null);

        sending = new TestMessage(".a test addon", getTestChannel());
        getTestChannel().expectMessage(new TestMessage(
                MessageUtils.getErrorMessage(sending.getId(), "No Addon Found", "No addons were found with that search"),
                getTestChannel()));
        sendMessage(sending);
        verify();

        TEST_DATA_FETCHER.addAddon("test addon", "test-addon");
        TEST_DATA_FETCHER.addData((Jsonable) Jsoner.deserialize(TEST_JSON));

        getTestChannel().expectMessage(new TestMessage(new EmbedBuilder(TEST_EMBED)
                .setFooter("Data provided by Duetro's Skript API | " + sending.getId()).build(), getTestChannel()));
        sendMessage(sending);
        verify();
    }
}
