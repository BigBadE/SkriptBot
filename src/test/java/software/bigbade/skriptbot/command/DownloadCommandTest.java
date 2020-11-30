package software.bigbade.skriptbot.command;

import org.junit.jupiter.api.Test;
import software.bigbade.skriptbot.commands.DownloadCommand;
import software.bigbade.skriptbot.testutils.TestChannel;
import software.bigbade.skriptbot.testutils.TestMessage;

class DownloadCommandTest {
    @Test
    void testDownloadCommand() {
        TestChannel testChannel = new TestChannel();
        testChannel.expectMessage(new TestMessage(DownloadCommand.EMBED, testChannel));
        new DownloadCommand().onCommand(testChannel, new String[0]);
        testChannel.verify();
    }
}
