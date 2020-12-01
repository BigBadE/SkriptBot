package software.bigbade.skriptbot.listeners;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import software.bigbade.skriptbot.api.ICommand;
import software.bigbade.skriptbot.utils.MessageUtils;
import software.bigbade.skriptbot.utils.StringUtils;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class ReactionListener extends ListenerAdapter {
    private static final Pattern MESSAGE_ID_SPLITTER = Pattern.compile("\\|");
    private final List<ICommand> commands;
    private final String prefix;
    @Setter
    private JDA jda;

    @Override
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
        if (!event.isFromGuild()) {
            return;
        }
        MessageReaction.ReactionEmote reaction = event.getReaction().getReactionEmote();
        if (!reaction.isEmoji() || event.getUser() == null || event.getUser().isBot()) {
            return;
        }
        event.retrieveMessage().queue(message -> handleReactionMessage(message, event.getUser(), reaction));
    }

    private void handleReactionMessage(Message message, User user, MessageReaction.ReactionEmote reaction) {
        if (!message.getAuthor().equals(jda.getSelfUser()) || message.getEmbeds().isEmpty()) {
            return;
        }

        String id = Objects.requireNonNull(message.getEmbeds().get(0).getFooter()).getText();

        assert id != null;

        if(id.indexOf('|') != -1) {
            id = MESSAGE_ID_SPLITTER.split(id, 2)[1].substring(1);
        }

        message.getChannel().retrieveMessageById(id).queue(lastMessage -> {
            if (reaction.getAsCodepoints().equals(MessageUtils.DELETE_REACTION)) {
                message.delete().queue();
                return;
            }

            String foundMessage = lastMessage.getContentStripped();
            if (!foundMessage.startsWith(prefix)) {
                return;
            }

            String foundCommand = CommandListener.getCommandName(prefix, foundMessage);
            for (ICommand command : commands) {
                if (StringUtils.matchesArray(foundCommand, command.getAliases())) {
                    command.onReaction(user, lastMessage, message, reaction);
                }
            }
        });
    }
}
