package software.bigbade.skriptbot.commands;

import java.awt.Color;
import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import software.bigbade.skriptbot.api.ICommand;
import software.bigbade.skriptbot.utils.MessageUtils;

public class PercentCommand implements ICommand {
  public static final EmbedBuilder PERCENT_EMBED =
      new EmbedBuilder()
          .setTitle("Percent signs (%)")
          .setColor(Color.green)
          .addField(new MessageEmbed.Field(
              "When should percent signs be used in Skript?",
              "In Skript, the purpose of surrounding an expression in % signs is letting Skript know "
                  +
                  "you want it to be parsed as an expression and insert its value into the string "
                  + "or variable you've put it in."
                  +
                  "organization a breeze. A list basically maps objects to their corresponding unique indices.",
              false))
          .addField(new MessageEmbed.Field(
              "Incorrect usage",
              "```yaml\n"
                  + "give dirt to %player%\n"
                  + "send \"Hey there player\" to %arg-1%\n"
                  + "kill %{_entity}%```",
              false))
          .addField(new MessageEmbed.Field(
              "Correct usage",
              "```yaml\n"
                  + "broadcast \"%player% has joined!\"\n"
                  + "send \"%{_variable::*}%\" to player\n"
                  + "set {_variable::%uuid of player%} to 10```",
              false));
  @Getter
  private final String[] aliases = new String[] {"percent", "percents", "%"};

  @Override
  public void onCommand(Member sender, TextChannel channel, String id,
                        String[] args) {
    MessageUtils.sendEmbedWithReaction(
        channel, new EmbedBuilder(PERCENT_EMBED)
                     .setFooter("Percent signs (%) | " + id)
                     .build());
  }
}
