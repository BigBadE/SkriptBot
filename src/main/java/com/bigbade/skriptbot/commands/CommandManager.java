package com.bigbade.skriptbot.commands;

import lombok.Getter;
import com.bigbade.skriptbot.api.ICommand;

import java.util.ArrayList;
import java.util.List;

public final class CommandManager {
    @Getter
    private final List<ICommand> commands = new ArrayList<>();

    public void registerCommand(ICommand command) {
        commands.add(command);
    }
}
