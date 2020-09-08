package software.bigbade.skriptbot.api;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;

public interface ICommand {
    void onCommand(TextChannel channel, String[] args);

    default void onReaction(Message command, Message message, MessageReaction.ReactionEmote emote) {}

    String[] getSymbol();
}
