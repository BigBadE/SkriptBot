package software.bigbade.skriptbot.command;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import software.bigbade.skriptbot.commands.DownloadCommand;

import java.util.function.Consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DownloadCommandTest {
    @SuppressWarnings("unchecked")
    @Test
    void testDownloadCommand() {
        DownloadCommand command = new DownloadCommand();
        TextChannel mockChannel = mock(TextChannel.class);
        ArgumentCaptor<Consumer<Message>> queueCaptor = ArgumentCaptor.forClass(Consumer.class);
        MessageAction action = mock(MessageAction.class);
        when(mockChannel.sendMessage(DownloadCommand.EMBED)).thenReturn(action);
        command.onCommand(mockChannel, new String[0]);
        verify(action).queue(queueCaptor.capture());
    }
}
