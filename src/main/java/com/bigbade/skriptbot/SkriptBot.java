package com.bigbade.skriptbot;

import com.bigbade.skriptbot.api.ICommand;
import com.bigbade.skriptbot.arguments.ProgramArguments;
import com.bigbade.skriptbot.listeners.CommandListener;
import com.bigbade.skriptbot.listeners.ReactionListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.logging.Logger;

@RequiredArgsConstructor
public final class SkriptBot {
    @Getter
    private static final Logger logger = Logger.getLogger(SkriptBot.class.getName());

    private final ProgramArguments arguments;
    private final List<ICommand> commands;

    public void init() throws LoginException {
        ReactionListener reactionListener = new ReactionListener(commands, arguments.getPrefix());
        JDA jda = JDABuilder
                .createDefault(arguments.getToken(), GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS)
                .disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOTE)
                .addEventListeners(new CommandListener(commands, arguments.getPrefix()), reactionListener).build();
        reactionListener.setJda(jda);
    }
}
