package software.bigbade.skriptbot.commands;

import lombok.Getter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import software.bigbade.skriptbot.api.ICommand;

public class SucksCommand implements ICommand {

    @Getter
    private final String[] aliases = new String[]{"sucks"};

    @Override
    public void onCommand(Member sender, TextChannel channel, String id, String[] args) {
        if (!sender.hasPermission(Permission.KICK_MEMBERS) || args.length == 0) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (String s : args) {
            builder.append(s).append(" ");
        }
        channel.sendMessage(builder.append("sucks by the way.").toString()).queue();
    }
}
