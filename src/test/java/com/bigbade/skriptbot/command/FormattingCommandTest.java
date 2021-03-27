package com.bigbade.skriptbot.command;

import com.bigbade.skriptbot.BasicCommandTestSetup;
import com.bigbade.skriptbot.commands.FormattingCommand;
import com.bigbade.skriptbot.testutils.TestMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import org.junit.jupiter.api.Test;

class FormattingCommandTest extends BasicCommandTestSetup<FormattingCommand> {
    @Test
    void testFormattingCommand() {
        TestMessage testMessage = new TestMessage(".format", getTestChannel());
        getTestChannel().expectMessage(new TestMessage(new EmbedBuilder(FormattingCommand.FORMATTING_EMBED)
                .setFooter("Formatting | " + testMessage.getId()).build(), getTestChannel()));

        sendMessage(testMessage);
        verify();
    }

    @Override
    public FormattingCommand getTestedCommand() {
        return new FormattingCommand();
    }
}
