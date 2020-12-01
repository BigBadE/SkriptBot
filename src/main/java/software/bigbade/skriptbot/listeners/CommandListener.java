package software.bigbade.skriptbot.listeners;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import software.bigbade.skriptbot.api.ICommand;
import software.bigbade.skriptbot.utils.RegexPatterns;
import software.bigbade.skriptbot.utils.StringUtils;

import java.util.List;
import java.util.StringTokenizer;

@RequiredArgsConstructor
public class CommandListener extends ListenerAdapter {
    private final List<ICommand> commands;
    private final String prefix;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        String message = event.getMessage().getContentStripped();
        if (!message.startsWith(prefix)) {
            return;
        }

        String foundCommand = getCommandName(prefix, message);
        for (ICommand command : commands) {
            if (StringUtils.matchesArray(foundCommand, command.getAliases())) {
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
                command.onCommand((TextChannel) event.getChannel(), event.getMessageId(), args);
                return;
            }
        }
    }

    public static String getCommandName(String prefix, String message) {
        return RegexPatterns.SPACE_PATTERN.split(message, 2)[0].substring(prefix.length());
    }
}
