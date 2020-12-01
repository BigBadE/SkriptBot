package software.bigbade.skriptbot.command;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.Jsonable;
import com.github.cliftonlabs.json_simple.Jsoner;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import software.bigbade.skriptbot.commands.AddonSearchCommand;
import software.bigbade.skriptbot.testutils.TestChannel;
import software.bigbade.skriptbot.testutils.TestIDHandler;
import software.bigbade.skriptbot.testutils.TestMessage;
import software.bigbade.skriptbot.testutils.TestResourceDataFetcher;
import software.bigbade.skriptbot.utils.MessageUtils;

import java.awt.Color;

class AddonSearchCommandTest {
    private static final TestResourceDataFetcher TEST_DATA_FETCHER = new TestResourceDataFetcher();
    private static final TestChannel TEST_MESSAGE_CHANNEL = new TestChannel();

    private static final AddonSearchCommand ADDON_SEARCH_COMMAND = new AddonSearchCommand(TEST_DATA_FETCHER, ".");

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

    @Test
    void testHumanReadableByteContentSI() {
        Assertions.assertEquals("12 B", AddonSearchCommand.humanReadableByteCountSI(12));
        Assertions.assertEquals("1.0 kB", AddonSearchCommand.humanReadableByteCountSI(1000));
        Assertions.assertEquals("1.9 kB", AddonSearchCommand.humanReadableByteCountSI(1949));
        Assertions.assertEquals("1.0 MB", AddonSearchCommand.humanReadableByteCountSI(1000000));
        Assertions.assertEquals("1.9 GB", AddonSearchCommand.humanReadableByteCountSI(1940000000));
    }

    @SneakyThrows(JsonException.class)
    @Test
    void testEmbedErrors() {
        String id = TestIDHandler.getId() + "";
        TEST_MESSAGE_CHANNEL.expectMessage(new TestMessage(
                MessageUtils.getErrorMessage(id, "No Addon Specified", "Usage: **.a skript-mirror**"),
                TEST_MESSAGE_CHANNEL));
        ADDON_SEARCH_COMMAND.onCommand(TEST_MESSAGE_CHANNEL, id, new String[0]);
        TEST_MESSAGE_CHANNEL.verify();

        TEST_DATA_FETCHER.addAddon("test addon", null);

        id = TestIDHandler.getId() + "";
        TEST_MESSAGE_CHANNEL.expectMessage(new TestMessage(
                MessageUtils.getErrorMessage(id, "No Addon Found", "No addons were found with that search"),
                TEST_MESSAGE_CHANNEL));
        ADDON_SEARCH_COMMAND.onCommand(TEST_MESSAGE_CHANNEL, id, new String[]{"test", "addon"});
        TEST_MESSAGE_CHANNEL.verify();

        TEST_DATA_FETCHER.addAddon("test addon", "test-addon");
        TEST_DATA_FETCHER.setData((Jsonable) Jsoner.deserialize(TEST_JSON));

        id = TestIDHandler.getId() + "";
        TEST_MESSAGE_CHANNEL.expectMessage(new TestMessage(new EmbedBuilder(TEST_EMBED)
                .setFooter("Data provided by Duetro's Skript API | " + id).build(), TEST_MESSAGE_CHANNEL));
        ADDON_SEARCH_COMMAND.onCommand(TEST_MESSAGE_CHANNEL, id, new String[]{"test", "addon"});
        TEST_MESSAGE_CHANNEL.verify();
    }
}
