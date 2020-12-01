package software.bigbade.skriptbot.command;

import net.dv8tion.jda.api.EmbedBuilder;
import org.junit.jupiter.api.Test;
import software.bigbade.skriptbot.commands.FormattingCommand;
import software.bigbade.skriptbot.testutils.TestChannel;
import software.bigbade.skriptbot.testutils.TestIDHandler;
import software.bigbade.skriptbot.testutils.TestMessage;

class FormattingCommandTest {
    @Test
    void testFormattingCommand() {
        TestChannel testChannel = new TestChannel();
        String id = TestIDHandler.getId() + "";
        testChannel.expectMessage(new TestMessage(new EmbedBuilder(FormattingCommand.FORMATTING_EMBED)
                .setFooter("Formatting | " + id).build(), testChannel));
        new FormattingCommand().onCommand(testChannel, id, new String[0]);
        testChannel.verify();
    }
}
