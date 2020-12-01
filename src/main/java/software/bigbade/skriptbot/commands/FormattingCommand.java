package software.bigbade.skriptbot.commands;

import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import software.bigbade.skriptbot.api.ICommand;
import software.bigbade.skriptbot.utils.MessageUtils;

import java.awt.Color;

public class FormattingCommand implements ICommand {
    public static final EmbedBuilder FORMATTING_EMBED = new EmbedBuilder().setTitle("Formatting Code In Discord")
            .setColor(Color.GREEN.getRGB()).addField(new MessageEmbed.Field("Why?",
                    "Code blocks make it easier for helpers to identify potential errors -- " +
                            "help them help you!", false))
            .addField(new MessageEmbed.Field("The Format", "\\`\\`\\`vb\n" +
                    "on chat:\n" +
                    "broadcast \"This is how you format code!\"" +
                    "\\`\\`\\`", true))
            .addField(new MessageEmbed.Field("How It Looks", "```vb\n" +
                    "on chat:\n" +
                    "broadcast \"This is how you format code!\"\n" +
                    "```", true))
            .addField(new MessageEmbed.Field("Extra Info", "On US keyboards, the grave character (`) is " +
                    "located above the tab key on the top left of the keyboard", false));
    @Getter
    private final String[] aliases = new String[]{"format", "formatting"};

    @Override
    public void onCommand(TextChannel channel, String id, String[] args) {
        MessageUtils.sendEmbedWithReaction(channel, new EmbedBuilder(FORMATTING_EMBED).setFooter("Format | " + id)
                .build());
    }
}
