package com.bigbade.skriptbot;

import com.bigbade.skriptbot.arguments.ArgumentParser;
import com.bigbade.skriptbot.arguments.ProgramArguments;
import com.bigbade.skriptbot.commands.AddonSearchCommand;
import com.bigbade.skriptbot.commands.CommandManager;
import com.bigbade.skriptbot.commands.DocSearchCommand;
import com.bigbade.skriptbot.commands.DownloadCommand;
import com.bigbade.skriptbot.commands.FormattingCommand;
import com.bigbade.skriptbot.commands.ListCommand;
import com.bigbade.skriptbot.commands.PastebinCommand;
import com.bigbade.skriptbot.commands.PercentCommand;
import com.bigbade.skriptbot.commands.SucksCommand;
import com.bigbade.skriptbot.commands.UUIDCommand;
import com.bigbade.skriptbot.utils.ResourceDataFetcher;

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
 * -- pastebinKey
 * Sets the Pastebin API key
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
        commandManager.registerCommand(new DocSearchCommand(dataFetcher, arguments.getPrefix()));
        commandManager.registerCommand(new PastebinCommand(arguments.getPastebinKey(), arguments.getPrefix()));
        commandManager.registerCommand(new UUIDCommand());
        commandManager.registerCommand(new FormattingCommand());
        commandManager.registerCommand(new DownloadCommand(dataFetcher));
        commandManager.registerCommand(new SucksCommand());
        commandManager.registerCommand(new ListCommand());
        commandManager.registerCommand(new PercentCommand());
    }
}
