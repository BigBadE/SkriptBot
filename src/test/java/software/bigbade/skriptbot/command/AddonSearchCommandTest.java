package software.bigbade.skriptbot.command;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.Jsoner;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import software.bigbade.skriptbot.commands.AddonSearchCommand;
import software.bigbade.skriptbot.testutils.TestMessage;
import software.bigbade.skriptbot.utils.MessageUtils;
import software.bigbade.skriptbot.utils.ResourceDataFetcher;

import java.awt.Color;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class AddonSearchCommandTest {
    private static final ResourceDataFetcher MOCK_DATA_FETCHER = mock(ResourceDataFetcher.class);
    private static final TextChannel MOCK_MESSAGE_CHANNEL = mock(TextChannel.class);
    private static final MockedStatic<MessageUtils> MOCKED_MESSAGE_UTILS = mockStatic(MessageUtils.class);
    private static final MockedStatic<ResourceDataFetcher> STATIC_MOCKED_DATA_FETCHER = mockStatic(ResourceDataFetcher.class);
    private static final AddonSearchCommand ADDON_SEARCH_COMMAND = new AddonSearchCommand(MOCK_DATA_FETCHER, ".");

    private static final MessageEmbed TEST_EMBED = new MessageEmbed("https://www.youtube.com/watch?v=dQw4w9WgXcQ",
            "SkTest 1.0.0", null, EmbedType.RICH, null, Color.GREEN.getRGB(),
            null, null, null, null,
            new MessageEmbed.Footer("Data provided by Duetro's Skript API", null, null), null,
            Arrays.asList(new MessageEmbed.Field("Addon", "**test-addon** by **Test, Testing, and BigBadE**", true),
                    new MessageEmbed.Field("Description", "Testing.", true),
                    new MessageEmbed.Field("Soft Depends", "Skript", true),
                    new MessageEmbed.Field("Download (1kb)", "https://www.youtube.com/watch?v=dQw4w9WgXcQ", false)
            ));

    private static final String TEST_JSON = "{\"success\": true,\"data\": {\"author\": [\"Test\", \"Testing\", \"BigBadE\"" +
            "],\"description\": \"Testing.\",\"plugin\": \"SkTest\",\"version\": \"1.0.0\",\"bytes\": \"1000\"," +
            "\"download\": \"https://www.youtube.com/watch?v=dQw4w9WgXcQ\",\"depend\": {\"softdepend\": [\"Skript\"]}}}";

    @AfterAll
    static void closeMockedStatic() {
        MOCKED_MESSAGE_UTILS.close();
        STATIC_MOCKED_DATA_FETCHER.close();
    }

    @AfterEach
    void resetMockedStatic() {
        MOCKED_MESSAGE_UTILS.reset();
        STATIC_MOCKED_DATA_FETCHER.reset();
    }

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
        MOCKED_MESSAGE_UTILS.when(() -> MessageUtils
                .getErrorMessage(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenCallRealMethod();
        ADDON_SEARCH_COMMAND.onCommand(MOCK_MESSAGE_CHANNEL, new String[0]);
        MOCKED_MESSAGE_UTILS.verify(() -> MessageUtils.sendEmbedWithReaction(MOCK_MESSAGE_CHANNEL,
                MessageUtils.getErrorMessage("No Addon Specified", "Usage: **.a skript-mirror**")));
        when(MOCK_DATA_FETCHER.getAddon("test addon")).thenReturn(Optional.empty());
        ADDON_SEARCH_COMMAND.onCommand(MOCK_MESSAGE_CHANNEL, new String[]{"test", "addon"});
        MOCKED_MESSAGE_UTILS.verify(() -> MessageUtils.sendEmbedWithReaction(MOCK_MESSAGE_CHANNEL,
                MessageUtils.getErrorMessage("No Addon Found", "No addons were found with that search")));
        when(MOCK_DATA_FETCHER.getAddon("test addon")).thenReturn(Optional.of("test-addon"));
        STATIC_MOCKED_DATA_FETCHER.when(() -> ResourceDataFetcher
                .readData("https://api.skripttools.net/v4/addons/test-addon"))
                .thenReturn(Optional.of(Jsoner.deserialize(TEST_JSON)));
        ADDON_SEARCH_COMMAND.onCommand(MOCK_MESSAGE_CHANNEL, new String[]{"test", "addon"});
        MOCKED_MESSAGE_UTILS.when(() -> MessageUtils.sendEmbedWithReaction(MOCK_MESSAGE_CHANNEL,
                ArgumentMatchers.any(MessageEmbed.class))).thenAnswer(answer -> {
            TestMessage.assertEmbedsEqual(TEST_EMBED, answer.getArgument(0));
            return null;
        });
    }
}
