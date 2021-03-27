package com.bigbade.skriptbot.listeners;

import com.bigbade.skriptbot.api.ICommand;
import com.bigbade.skriptbot.commands.DocSearchCommand;
import com.bigbade.skriptbot.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.List;
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

    @SuppressWarnings("ConstantConditions")
    private void handleReactionMessage(Message message, User user, MessageReaction.ReactionEmote reaction) {
        if (!message.getAuthor().equals(jda.getSelfUser()) || message.getEmbeds().isEmpty()) {
            System.out.println(1);
            return;
        }

        String id = message.getEmbeds().get(0).getFooter().getText();

        assert id != null;

        if(id.indexOf('|') != -1) {
            id = MESSAGE_ID_SPLITTER.split(id, 2)[1].substring(1);
        }

        message.getChannel().retrieveMessageById(id).queue(lastMessage -> {
            if(!lastMessage.getAuthor().equals(user)) {
                return;
            }

            if (reaction.getAsCodepoints().equalsIgnoreCase(MessageUtils.DELETE_REACTION)) {
                message.delete().queue(DocSearchCommand::ignoreInput, DocSearchCommand::ignoreInput);
                return;
            }

            String foundMessage = lastMessage.getContentStripped();
            if (!foundMessage.startsWith(prefix)) {
                return;
            }

            String foundCommand = CommandListener.getCommandName(prefix, foundMessage);
            for (ICommand command : commands) {
                if (command.getAliases().contains(foundCommand)) {
                    command.onReaction(user, lastMessage, message, reaction);
                    return;
                }
            }
        });
    }
}
