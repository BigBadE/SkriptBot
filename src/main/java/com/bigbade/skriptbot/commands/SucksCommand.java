package com.bigbade.skriptbot.commands;

import com.bigbade.skriptbot.api.ICommand;
import com.bigbade.skriptbot.utils.RegexPatterns;
import lombok.Getter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

import java.util.Collections;
import java.util.List;

public class SucksCommand implements ICommand {

    @Getter
    private final List<String> aliases = Collections.singletonList("sucks");

    @Override
    public void onCommand(Message message, String[] args) {
        //Checked by command handler
        assert message.getMember() != null;
        if (!message.getMember().hasPermission(Permission.KICK_MEMBERS) || args.length == 0) {
            return;
        }
        message.getChannel().sendMessage(RegexPatterns.SPACE_PATTERN.split(message.getContentRaw(), 2)[1]
                + " sucks by the way.").queue();
    }
}
