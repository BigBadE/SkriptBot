package software.bigbade.skriptbot.api;

import net.dv8tion.jda.api.entities.*;

public interface ICommand {
    void onCommand(Member sender, TextChannel channel, String id, String[] args);

    default void onReaction(User reactor, Message command, Message message, MessageReaction.ReactionEmote emote) {}

    String[] getAliases();
}
