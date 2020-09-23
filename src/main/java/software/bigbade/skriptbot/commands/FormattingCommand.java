package software.bigbade.skriptbot.commands;

import lombok.Getter;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import software.bigbade.skriptbot.api.ICommand;
import software.bigbade.skriptbot.utils.MessageUtils;

import java.awt.Color;
import java.util.Arrays;

public class FormattingCommand implements ICommand {
    public static final MessageEmbed FORMATTING_EMBED = new MessageEmbed(null, "Formatting Code In Discord",
            null, EmbedType.RICH, null, Color.GREEN.getRGB(),
            null, null, null, null,
            new MessageEmbed.Footer("Format", null, null), null,
            Arrays.asList(new MessageEmbed.Field("Why?",
                            "Code blocks make it easier for helpers to identify potential errors -- " +
                                    "help them help you!", false),
                    new MessageEmbed.Field("The Format", "\\`\\`\\`vb\n" +
                            "on chat:\n" +
                            "broadcast \"This is how you format code!\"" +
                            "\\`\\`\\`", true),
                    new MessageEmbed.Field("How It Looks", "```vb\n" +
                            "on chat:\n" +
                            "broadcast \"This is how you format code!\"\n" +
                            "```", true),
                    new MessageEmbed.Field("Extra Info", "On US keyboards, the grave character (`) is " +
                            "located above the tab key on the top left of the keyboard", false)));
    @Getter
    private final String[] aliases = new String[]{"format", "formatting"};

    @Override
    public void onCommand(TextChannel channel, String[] args) {
        MessageUtils.sendEmbedWithReaction(channel, FORMATTING_EMBED);
    }
}
