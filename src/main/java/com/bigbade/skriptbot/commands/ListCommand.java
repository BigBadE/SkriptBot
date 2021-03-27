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

public class ListCommand implements ICommand {
    public static final EmbedBuilder LIST_EMBED = new EmbedBuilder().setTitle("List Variables")
            .setColor(Color.green).addField(new MessageEmbed.Field("Why?",
                    "List variables are a much cleaner way of storing multiple values, especially objects "
                            + "that are unique to something (the money of a player, the warps of the server), as they "
                            + "can be looped, added to, removed from, and deleted all at once, making variable "
                            + "organization a breeze. A list basically maps objects to their corresponding unique indices.", false))
            .addField(new MessageEmbed.Field("How to create a list variable?",
                    "To make a list, we simply use the list variable separator `::`, `{money::%uuid of player%}`, "
                            + "{warps::%{_warpName}%}, {luckyNumbers::*}.", false))
            .addField(new MessageEmbed.Field("Indices and values",
                    "As already mentioned, lists have indices and values. For instance, in "
                            + "`set {money::%uuid of player%} to 100` the index is the uuid of the player and the value is 100. "
                            + "We can also access all the values at once by using ::*.", false))
            .addField(new MessageEmbed.Field("Common situations which can use lists instead",
                    "```yaml\n" +
                            "{%player%.money} -> {money::%player's uuid%}\n"
                            + "{home.warps.%player%} -> {warps::%player's uuid%::home}\n"
                            + "{%player%.cooldown} -> {cooldown::%player's uuid%}```", false));
    @Getter
    private final List<String> aliases = Collections.unmodifiableList(Arrays.asList("list", "lists"));

    @Override
    public void onCommand(Message message, String[] args) {
        MessageUtils.sendEmbedWithReaction(message.getTextChannel(), new EmbedBuilder(LIST_EMBED)
                .setFooter("Lists | " + message.getId()).build());
    }
}
