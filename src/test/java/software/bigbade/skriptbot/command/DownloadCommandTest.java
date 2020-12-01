package software.bigbade.skriptbot.command;

import net.dv8tion.jda.api.EmbedBuilder;
import org.junit.jupiter.api.Test;
import software.bigbade.skriptbot.commands.DownloadCommand;
import software.bigbade.skriptbot.testutils.TestChannel;
import software.bigbade.skriptbot.testutils.TestIDHandler;
import software.bigbade.skriptbot.testutils.TestMessage;

class DownloadCommandTest {
    @Test
    void testDownloadCommand() {
        TestChannel testChannel = new TestChannel();
        String id = TestIDHandler.getId() + "";
        testChannel.expectMessage(new TestMessage(new EmbedBuilder(DownloadCommand.EMBED).setFooter("Downloads | " + id)
                .build(), testChannel));
        new DownloadCommand().onCommand(testChannel, id, new String[0]);
        testChannel.verify();
    }
}
