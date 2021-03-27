package com.bigbade.skriptbot.listeners;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import com.bigbade.skriptbot.api.ICommand;
import com.bigbade.skriptbot.utils.RegexPatterns;

import java.util.List;
import java.util.StringTokenizer;

@RequiredArgsConstructor
public class CommandListener extends ListenerAdapter {
    private final List<ICommand> commands;
    private final String prefix;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        //Drop webhooks and bots
        if (event.getMember() == null || event.getAuthor().isBot()) {
            return;
        }
        String message = event.getMessage().getContentStripped();
        if (!message.startsWith(prefix)) {
            return;
        }

        String foundCommand = getCommandName(prefix, message);
        for (ICommand command : commands) {
            if (command.getAliases().contains(foundCommand)) {
                runCommand(event.getMessage(), command);
                return;
            }
        }
    }

    private static void runCommand(Message message, ICommand command) {
        int size = 0;
        for(char character : message.getContentStripped().toCharArray()) {
            if(character == ' ') {
                size++;
            }
        }
        String[] args = new String[size];
        StringTokenizer tokenizer = new StringTokenizer(message.getContentRaw(), " ");
        //Skip the command
        tokenizer.nextToken();

        for(int i = 0; i < size; i++) {
            String arg = tokenizer.nextToken();
            args[i] = arg;
        }
        command.onCommand(message, args);
    }

    public static String getCommandName(String prefix, String message) {
        return RegexPatterns.SPACE_PATTERN.split(message, 2)[0].substring(prefix.length());
    }
}
