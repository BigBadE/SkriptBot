package com.bigbade.skriptbot.commands;

import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import com.bigbade.skriptbot.api.ICommand;
import com.bigbade.skriptbot.utils.MessageUtils;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PercentCommand implements ICommand {
    public static final EmbedBuilder PERCENT_EMBED = new EmbedBuilder().setTitle("Percent signs (%)")
            .setColor(Color.green).addField(new MessageEmbed.Field("When should percent signs be used in Skript?",
                    "In Skript, the purpose of surrounding an expression in % signs is letting Skript know "
                            + "you want it to be parsed as an expression and insert its value into the string "
                            + "or variable you've put it in.", false))
            .addField(new MessageEmbed.Field("Incorrect usage",
                    "```yaml\n"
                            + "give dirt to %player%\n"
                            + "send \"Hey there player\" to %arg-1%\n"
                            + "kill %{_entity}%```", false))
            .addField(new MessageEmbed.Field("Correct usage",
                    "```yaml\n" +
                            "broadcast \"%player% has joined!\"\n"
                            + "send \"%{_variable::*}%\" to player\n"
                            + "set {_variable::%uuid of player%} to 10```", false));
    @Getter
    private final List<String> aliases = Collections.unmodifiableList(Arrays.asList("percent", "percents", "%"));

    @Override
    public void onCommand(Message message, String[] args) {
        MessageUtils.sendEmbedWithReaction(message.getTextChannel(), new EmbedBuilder(PERCENT_EMBED)
                .setFooter("Percent signs (%) | " + message.getId()).build());
    }
}
