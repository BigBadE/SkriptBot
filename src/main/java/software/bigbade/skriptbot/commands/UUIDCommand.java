package software.bigbade.skriptbot.commands;

import java.awt.*;
import java.util.Arrays;
import lombok.Getter;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import software.bigbade.skriptbot.api.ICommand;
import software.bigbade.skriptbot.utils.MessageUtils;

public class UUIDCommand implements ICommand {

  public static final MessageEmbed UUID_EMBED = new MessageEmbed(
      null, "UUIDs in variables", null, EmbedType.RICH, null,
      Color.green.getRGB(), null, null, null, null,
      new MessageEmbed.Footer("UUID", null, null), null,
      Arrays.asList(
          new MessageEmbed.Field(
              "Why?",
              "UUIDs are a much better way to store your variables, as a player's UUID will never "
                  +
                  "change, whereas their name can. UUIDs can be used for any type of variable, in the "
                  +
                  "same way as `%player%` is. Your player's money/stats can be lost if they change their in "
                  + "game name when `%player%` is used.",
              false),
          new MessageEmbed.Field(
              "How to use",
              "You use this in the same was as using `%player%`, just with "
                  + "`'s uuid` on the end, so `player's uuid`!",
              false),
          new MessageEmbed.Field(
              "Common situations which can use UUID's instead",
              "```vb\n"
                  + "{stats::%player%} -> {stats::%player's uuid%}\n"
                  + "{vanish::%player%} -> {vanish::%uuid of player%}\n"
                  +
                  "{generator::%player%::*} -> {generator::%player's uuid%::*}```",
              false)));
  @Getter private final String[] aliases = new String[] {"uuid", "uuids"};

  @Override
  public void onCommand(TextChannel channel, String[] args) {
    MessageUtils.sendEmbedWithReaction(channel, UUID_EMBED);
  }
}
