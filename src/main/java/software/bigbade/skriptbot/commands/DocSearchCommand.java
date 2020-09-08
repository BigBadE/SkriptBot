package software.bigbade.skriptbot.commands;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import software.bigbade.skriptbot.api.ICommand;
import software.bigbade.skriptbot.utils.HTMLUtilities;
import software.bigbade.skriptbot.utils.JsonKeys;
import software.bigbade.skriptbot.utils.MessageUtils;
import software.bigbade.skriptbot.utils.RegexPatterns;
import software.bigbade.skriptbot.utils.ResourceDataFetcher;

import java.awt.Color;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@RequiredArgsConstructor
public class DocSearchCommand implements ICommand {
    private static final String[] aliases = new String[]{"d", "doc", "docs", "documentation"};
    private static final String ARROW_LEFT = "U+20C0";
    private static final String ARROW_RIGHT = "U+25B6";
    private static final String FOOTER = "Powered by skUnity Docs 2";
    private final ResourceDataFetcher dataFetcher;

    private static int getNumberFromString(String string) {
        char character;
        int index = 0;
        int found = 0;
        while ((character = string.charAt(index++)) != '0') {
            found *= 10;
            found += character - '0';
        }
        return found;
    }

    private static void removeReactions(Message message, boolean leftArrow) {
        for (int i = 0; i < 10; i++) {
            message.removeReaction(getNumberEmote(i)).queue();
        }
        if (leftArrow) {
            message.removeReaction(ARROW_LEFT).queue();
        }
        message.removeReaction(ARROW_RIGHT).queue();
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    @Override
    public void onCommand(TextChannel channel, String[] args) {
        if (args.length == 0) {
            MessageUtils.sendEmbedWithReaction(channel, MessageUtils.getErrorMessage("No Syntax Specified",
                    "Usage: **.doc subtext**"));
            return;
        }

        String query = String.join(" ", args);
        JsonArray array = dataFetcher.getDocsResults(query);
        if(array.isEmpty()) {
            MessageUtils.sendEmbedWithReaction(channel, MessageUtils.getErrorMessage("No results\n",
                    "No results were found for that query"));
            return;
        }
        if(array.size() == 1) {
            channel.sendMessage(getResponse(array, 0)).queue();
            return;
        }
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setFooter(FOOTER)
                .setTitle("Found Results", "https://docs.skunity.com/syntax/search/"
                        + URLEncoder.encode(query, StandardCharsets.UTF_8.name()));
        addDocsResponse(builder, array, 0);
        channel.sendMessage(builder.build())
                .queue(response -> {
                    response.addReaction(MessageUtils.DELETE_REACTION).queue();
                    for (int i = 0; i < Math.min(array.size(), 10); i++) {
                        response.addReaction(getNumberEmote(i)).queue();
                    }
                    if (array.size() >= 10) {
                        response.addReaction(ARROW_RIGHT).queue();
                    }
                });
    }

    public void addDocsResponse(EmbedBuilder builder, JsonArray array, int start) {
        for (int i = start; i < Math.min(array.size(), start + 10); i++) {
            JsonObject result = (JsonObject) array.get(i);
            String doc = result.getString(JsonKeys.DOC.getKey());
            builder.addField(i + ". " + result.getString(JsonKeys.NAME.getKey()) + " ("
                            + doc.substring(0, doc.length() - 1) + ")",
                    result.getString(JsonKeys.PATTERN.getKey()), false);
        }
    }

    public MessageEmbed getResponse(String query, int index) {
        JsonArray array = dataFetcher.getDocsResults(query);
        return getResponse(array, index);
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    private static MessageEmbed getResponse(JsonArray array, int index) {
        JsonObject object = (JsonObject) array.get(index);
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle(object.getString(JsonKeys.NAME.getKey()),
                        "https://docs.skunity.com/syntax/search/"
                                + URLEncoder.encode(object.getString(JsonKeys.NAME.getKey()), StandardCharsets.UTF_8.name()))
                .setFooter(FOOTER)
                .setColor(Color.YELLOW)
                .addField("Pattern", "```" + object.getString(JsonKeys.PATTERN.getKey()) + "```", false);
        String description = object.getString(JsonKeys.DESC.getKey());
        if (!description.isEmpty()) {
            builder.addField("Description", object.getString(JsonKeys.DESC.getKey()), false);
        }
        JsonArray examples = (JsonArray) object.get("examples");
        if (!examples.isEmpty()) {
            builder.addField("Example", "```" + HTMLUtilities.unescapeHtml(((JsonObject) examples.get(0)).getString(JsonKeys.EXAMPLE.getKey())) + "```", false);
        }
        String requires = object.getString(JsonKeys.PLUGIN.getKey());
        if (requires.isEmpty()) {
            requires = "Skript";
        }
        String addon = object.getString(JsonKeys.ADDON.getKey());
        if (!addon.isEmpty()) {
            builder.addField("Addon", addon, true);
        }
        builder.addField("Requires", requires, true);
        return builder.build();
    }

    @Override
    public void onReaction(Message command, Message message, MessageReaction.ReactionEmote emote) {
        if (!emote.isEmoji()) {
            return;
        }
        String query = RegexPatterns.SPACE_PATTERN.split(command.getContentDisplay(), 2)[1];
        int found = getNumberFromString(Objects.requireNonNull(message.getEmbeds().get(0).getFields().get(0).getName()));
        if (emote.getAsCodepoints().equals(ARROW_LEFT)) {
            addReactions(message, query, found);
        } else if (emote.getAsCodepoints().equals(ARROW_RIGHT)) {
            addReactions(message, query, found+10);
        } else {
            String codepoint = emote.getAsCodepoints();
            if (codepoint.length() != 16) {
                return;
            }
            char foundChar = codepoint.charAt(3);
            if (foundChar >= '0' && foundChar <= '9') {
                message.getChannel().sendMessage(getResponse(query, found + (foundChar - '0'))).queue();
            }
        }
    }

    private void addReactions(Message message, String query, int index) {
        removeReactions(message, (index >= 20));
        JsonArray array = dataFetcher.getDocsResults(query);
        EmbedBuilder builder = new EmbedBuilder().setFooter(FOOTER).setColor(Color.YELLOW);
        addDocsResponse(builder, array, index);
        message.editMessage(builder.build()).queue();
        if (index > 9) {
            message.addReaction(ARROW_LEFT).queue();
        }
        for (int i = 0; i < array.size()-index; i++) {
            message.addReaction(getNumberEmote(i)).queue();
        }
        if (array.size() > index+9) {
            message.addReaction(ARROW_RIGHT).queue();
        }
    }

    private static String getNumberEmote(int number) {
        return "U+3" + number + "U+fe0fU+20e3";
    }

    @Override
    public String[] getAliases() {
        return aliases;
    }
}
