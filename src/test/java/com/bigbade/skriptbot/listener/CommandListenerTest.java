package com.bigbade.skriptbot.listener;

import com.bigbade.skriptbot.api.ICommand;
import com.bigbade.skriptbot.listeners.CommandListener;
import com.bigbade.skriptbot.testutils.TestJDA;
import com.bigbade.skriptbot.testutils.TestMember;
import com.bigbade.skriptbot.testutils.TestUser;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.internal.entities.ReceivedMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

class CommandListenerTest {
    private static final JDA jda = TestJDA.TEST_JDA;
    private static final CommandListener listener;
    @Setter
    private static boolean commandRan;

    static {
        ICommand command = new ICommand() {
            @Getter
            private final List<String> aliases = Collections.singletonList("test");

            @Override
            public void onCommand(Message message, String[] args) {
                if(args.length == 3 && args[0].equals("all") && args[1].equals("the") && args[2].equals("args")) {
                    setCommandRan(true);
                }
            }
        };
        listener = new CommandListener(Collections.singletonList(command), ".");
    }

    @BeforeEach
    void prepareCommandRanTest() {
        setCommandRan(false);
    }

    @Test
    void wrongCommandReceivedTest() {
        listener.onMessageReceived(new MessageReceivedEvent(jda, 0,
                constructEvent(".test all the", true)));
        listener.onMessageReceived(new MessageReceivedEvent(jda, 0,
                constructEvent("test all the args", false)));
        listener.onMessageReceived(new MessageReceivedEvent(jda, 0,
                constructEvent(".tes all the args", false)));
        listener.onMessageReceived(new MessageReceivedEvent(jda, 0,
                constructEvent(".testing all the args", false)));
        Assertions.assertFalse(commandRan);
    }

    @Test
    void correctCommandReceivedTest() {
        listener.onMessageReceived(new MessageReceivedEvent(jda, 0, constructEvent(".test all the args", false)));
        Assertions.assertTrue(commandRan);
    }

    private Message constructEvent(String message, boolean isBot) {
        TestUser author = new TestUser("TestUser");
        author.setBot(isBot);
        return new ReceivedMessage(0, null, MessageType.DEFAULT, null,
                false, false, null, null, false, false,
                message, null, author, new TestMember(author), null, null, Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(), 0);
    }
}
