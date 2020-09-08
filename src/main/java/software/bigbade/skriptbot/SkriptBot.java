package software.bigbade.skriptbot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bigbade.skriptbot.api.ICommand;
import software.bigbade.skriptbot.arguments.ProgramArguments;
import software.bigbade.skriptbot.listeners.CommandListener;
import software.bigbade.skriptbot.listeners.ReactionListener;

import javax.security.auth.login.LoginException;
import java.util.List;

@RequiredArgsConstructor
public final class SkriptBot {
    @Getter
    private static final Logger logger = LoggerFactory.getLogger(SkriptBot.class);

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
