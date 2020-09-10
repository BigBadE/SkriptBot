package software.bigbade.skriptbot;

import software.bigbade.skriptbot.arguments.ArgumentParser;
import software.bigbade.skriptbot.arguments.ProgramArguments;
import software.bigbade.skriptbot.commands.AddonSearchCommand;
import software.bigbade.skriptbot.commands.CommandManager;
import software.bigbade.skriptbot.commands.DocSearchCommand;
import software.bigbade.skriptbot.commands.DownloadCommand;
import software.bigbade.skriptbot.commands.FormattingCommand;
import software.bigbade.skriptbot.utils.ResourceDataFetcher;

import javax.security.auth.login.LoginException;

/**
 * Rewrite of Pikachu's Skript Discord Bot in Java.
 * Arguments:
 *
 * --token
 * Sets the token of the bot
 * --debug
 * Enables debug messages
 * --prefix
 * Sets bot prefix, default is "."
 * --skUnityKey
 * Sets the SkUnity token
 */
public class Main {
    private static final CommandManager commandManager = new CommandManager();
    private static ProgramArguments arguments;

    public static void main(String[] args) {
        arguments = new ArgumentParser(args).getArguments();
        registerCommands();
        SkriptBot skriptBot = new SkriptBot(arguments, commandManager.getCommands());
        try {
            skriptBot.init();
        } catch (LoginException e) {
            throw new IllegalArgumentException("Invalid token!");
        }
    }

    private static void registerCommands() {
        ResourceDataFetcher dataFetcher = new ResourceDataFetcher(arguments.getSkUnityKey());
        commandManager.registerCommand(new AddonSearchCommand(dataFetcher, arguments.getPrefix()));
        commandManager.registerCommand(new DocSearchCommand(dataFetcher));
        commandManager.registerCommand(new FormattingCommand());
        commandManager.registerCommand(new DownloadCommand());
    }
}
