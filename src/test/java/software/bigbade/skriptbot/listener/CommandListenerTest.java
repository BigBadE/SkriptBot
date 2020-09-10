package software.bigbade.skriptbot.listener;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.internal.entities.ReceivedMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.bigbade.skriptbot.api.ICommand;
import software.bigbade.skriptbot.listeners.CommandListener;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommandListenerTest {
    private static final JDA jda = mock(JDA.class);
    private static final CommandListener listener;
    private static boolean commandRan;

    static {
        ICommand command = new ICommand() {
            @Override
            public void onCommand(TextChannel channel, String[] args) {
                if(args.length == 3 && args[0].equals("all") && args[1].equals("the") && args[2].equals("args")) {
                    commandRan = true;
                }
            }

            @Override
            public String[] getAliases() {
                return new String[]{"test"};
            }
        };
        listener = new CommandListener(Collections.singletonList(command), ".");
    }

    @BeforeEach
    void prepareCommandRanTest() {
        commandRan = false;
    }

    @Test
    void wrongCommandReceivedTest() {
        listener.onMessageReceived(new MessageReceivedEvent(jda, 0, constructEvent(".test", true)));
        listener.onMessageReceived(new MessageReceivedEvent(jda, 0, constructEvent("test", false)));
        listener.onMessageReceived(new MessageReceivedEvent(jda, 0, constructEvent(".tes", false)));
        listener.onMessageReceived(new MessageReceivedEvent(jda, 0, constructEvent(".testing", false)));
        Assertions.assertFalse(commandRan);
    }

    @Test
    void correctCommandReceivedTest() {
        listener.onMessageReceived(new MessageReceivedEvent(jda, 0, constructEvent(".test all the args", false)));
        Assertions.assertTrue(commandRan);
    }

    private Message constructEvent(String message, boolean isBot) {
        User author = mock(User.class);
        when(author.isBot()).thenReturn(isBot);
        MessageChannel channel = mock(TextChannel.class);
        when(channel.getType()).thenReturn(ChannelType.TEXT);
        return new ReceivedMessage(0, channel, MessageType.DEFAULT, false,
                false, null, null, false, false, message, null,
                author, null, null, null, Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), 0);
    }
}
