package com.bigbade.skriptbot.api;

import net.dv8tion.jda.api.entities.*;

import java.util.List;

public interface ICommand {
    void onCommand(Message message, String[] args);

    default void onReaction(User reactor, Message command, Message message, MessageReaction.ReactionEmote emote) {}

    List<String> getAliases();
}
