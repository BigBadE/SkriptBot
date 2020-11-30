package software.bigbade.skriptbot.command;

import org.junit.jupiter.api.Test;
import software.bigbade.skriptbot.commands.FormattingCommand;
import software.bigbade.skriptbot.testutils.TestChannel;
import software.bigbade.skriptbot.testutils.TestMessage;

class FormattingCommandTest {
    @Test
    void testFormattingCommand() {
        TestChannel testChannel = new TestChannel();
        testChannel.expectMessage(new TestMessage(FormattingCommand.FORMATTING_EMBED, testChannel));
        new FormattingCommand().onCommand(testChannel, new String[0]);
        testChannel.verify();
    }
}
