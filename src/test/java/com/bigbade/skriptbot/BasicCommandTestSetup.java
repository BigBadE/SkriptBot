package com.bigbade.skriptbot;

import com.bigbade.skriptbot.api.ICommand;
import lombok.Getter;
import net.dv8tion.jda.api.entities.MessageReaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import com.bigbade.skriptbot.testutils.TestChannel;
import com.bigbade.skriptbot.testutils.TestMessage;
import com.bigbade.skriptbot.testutils.TestResourceDataFetcher;
import com.bigbade.skriptbot.testutils.TestUser;

import java.util.StringTokenizer;

public abstract class BasicCommandTestSetup<T extends ICommand> {
    @Getter
    private final TestResourceDataFetcher testDataFetcher = new TestResourceDataFetcher();
    @Getter
    private final TestChannel testChannel = new TestChannel();

    @Getter
    private T command;

    @BeforeEach
    public void setup() {
        command = getTestedCommand();
    }

    @AfterEach
    public void teardown() {
        command = null;
    }

    public void sendMessage(TestMessage message) {
        command.onCommand(message, extractArgs(message.getContentRaw()));
    }

    public void addReaction(TestMessage message, TestMessage response, TestUser user, MessageReaction.ReactionEmote reactionEmote) {
        command.onReaction(user, message, response, reactionEmote);
    }

    private String[] extractArgs(String message) {
        int size = 0;
        for(char character : message.toCharArray()) {
            if(character == ' ') {
                size++;
            }
        }
        String[] args = new String[size];
        StringTokenizer tokenizer = new StringTokenizer(message, " ");
        //Skip the command
        tokenizer.nextToken();

        for(int i = 0; i < size; i++) {
            String arg = tokenizer.nextToken();
            args[i] = arg;
        }
        return args;
    }

    public void verify() {
        testChannel.verify();
    }

    public abstract T getTestedCommand();
}
