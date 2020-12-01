package software.bigbade.skriptbot.commands;

import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import software.bigbade.skriptbot.api.ICommand;
import software.bigbade.skriptbot.utils.MessageUtils;

import java.awt.Color;

public class DownloadCommand implements ICommand {
    @Getter
    private final String[] aliases = new String[] { "down", "download", "downlaods" };

    public static final EmbedBuilder EMBED = new EmbedBuilder().setTitle("Downloading Skript")
            .setColor(Color.GREEN).addField("1.7", "Recommended but **not supported**:\n\n"
                    + "[Njol, 2.2-SNAPSHOT](https://github.com/Pikachu920/Skript/releases/download/2.2-SNAPSHOT/Skript-2.2-Njol.jar)", true)
            .addField("1.8", "Recommended but **not supported**:\n\n"
                    + "[Bensku fork, dev36](https://github.com/SkriptLang/Skript/releases/download/dev36/Skript.jar)", true)
            .addField("1.9+", "Recommended: \n\n"
                    + "[Bensku Fork 2.4.1](https://github.com/SkriptLang/Skript/releases/download/2.4.1/Skript.jar)\n\n"
                    + "You can also find the latest version of Bensku's fork (2.5-beta3) "
                    + "[here](https://github.com/SkriptLang/Skript/releases/download/2.5-beta3/Skript.jar), "
                    + "but it might be unstable", true)
            .addField("Extra Info", "Note that all versions of Skript not by Njol are unofficial and " +
                    "their issues should be reported to their respective issue trackers", false)
            .setFooter("Downloads | ");

    @Override
    public void onCommand(TextChannel channel, String id, String[] args) {
        MessageUtils.sendEmbedWithReaction(channel, new EmbedBuilder(EMBED).setFooter("Downloads | " + id).build());
    }
}
