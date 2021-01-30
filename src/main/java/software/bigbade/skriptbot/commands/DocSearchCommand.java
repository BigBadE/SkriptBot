package software.bigbade.skriptbot.commands;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import software.bigbade.skriptbot.api.ICommand;
import software.bigbade.skriptbot.api.IDataFetcher;
import software.bigbade.skriptbot.utils.HTMLUtilities;
import software.bigbade.skriptbot.utils.JsonKeys;
import software.bigbade.skriptbot.utils.MessageUtils;
import software.bigbade.skriptbot.utils.RegexPatterns;

import java.awt.Color;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class DocSearchCommand implements ICommand {
    public static final String FOOTER = "Powered by skUnity Docs 2 | ";
    public static final String ARROW_LEFT = "U+25c0";
    public static final String ARROW_RIGHT = "U+25b6";
    @Getter
    private final String[] aliases = new String[]{"d", "doc", "docs", "documentation"};
    private final IDataFetcher dataFetcher;
    private final String prefix;

    /**
     * Converts string starting with a number that ends with a .
     * to an integer.
     *
     * @param string String that starts with a number
     * @return Number from string
     */
    public static int getNumberFromString(String string) {
        char character;
        int index = 0;
        int found = 0;
        while ((character = string.charAt(index++)) != '.') {
            found *= 10;
            found += character - '0';
        }
        return found;
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    public static MessageEmbed getResponse(JsonArray array, String id, int index) {
        JsonObject object = (JsonObject) array.get(index);
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle(object.getString(JsonKeys.NAME.getKey()),
                        "https://docs.skunity.com/syntax/search/"
                                + URLEncoder.encode(object.getString(JsonKeys.NAME.getKey()), StandardCharsets.UTF_8.name()))
                .setFooter(FOOTER + id)
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
        String addon = object.getString(JsonKeys.ADDON.getKey());
        if (!addon.isEmpty()) {
            builder.addField("Addon", addon, true);
        }
        String requires = object.getString(JsonKeys.PLUGIN.getKey());
        if (requires.isEmpty()) {
            requires = "Skript";
        }
        builder.addField("Requires", requires, true);
        return builder.build();
    }

    public static String getNumberEmote(int number) {
        if (number < 0 || number > 9) {
            throw new IllegalArgumentException("Number outside of range [0, 9]");
        }
        return "U+3" + number + "U+fe0fU+20e3";
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    @Override
    public void onCommand(Member sender, TextChannel channel, String id, String[] args) {
        if (args.length == 0) {
            MessageUtils.sendEmbedWithReaction(channel, MessageUtils.getErrorMessage(id, "No Syntax Specified",
                    "Usage: **" + prefix + "doc subtext**"));
            return;
        }

        String query = String.join(" ", args);
        JsonArray array = dataFetcher.getDocsResults(query);
        if (array.isEmpty()) {
            MessageUtils.sendEmbedWithReaction(channel, MessageUtils.getErrorMessage(id, "No Results",
                    "No results were found for that query"));
            return;
        }
        if (array.size() == 1) {
            MessageUtils.sendEmbedWithReaction(channel, getResponse(array, id, 0));
            return;
        }
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setFooter(FOOTER + id)
                .setTitle("Found Results", "https://docs.skunity.com/syntax/search/"
                        + URLEncoder.encode(query, StandardCharsets.UTF_8.name()));
        addDocsResponse(builder, array, 0);
        channel.sendMessage(builder.build()).queue(response -> queNextAction(response, new AtomicInteger(0)));
    }

    private static void queNextAction(Message message, AtomicInteger current) {
        String reaction;
        int value = current.getAndIncrement();
        if(value < 10) {
            reaction = getNumberEmote(value);
        } else if(value == 11) {
            reaction = MessageUtils.DELETE_REACTION;
        } else if(value == 12) {
            reaction = ARROW_LEFT;
        } else if(value == 13) {
            reaction = ARROW_RIGHT;
        } else {
            return;
        }

        message.addReaction(reaction).queue(none -> queNextAction(message, current), throwable -> {});
    }

    public void addDocsResponse(EmbedBuilder builder, JsonArray array, int start) {
        for (int i = start; i < Math.min(array.size(), start + 10); i++) {
            JsonObject result = (JsonObject) array.get(i);
            String doc = result.getString(JsonKeys.DOC.getKey());
            builder.addField(i + ". " + result.getString(JsonKeys.NAME.getKey()) + " ("
                            + doc.substring(0, doc.length() - 1) + ")",
                    "```" + result.getString(JsonKeys.PATTERN.getKey()) + "```", false);
        }
    }

    @Override
    public void onReaction(User reactor, Message command, Message message, MessageReaction.ReactionEmote emote) {
        if (!emote.isEmoji()) {
            return;
        }

        String query = RegexPatterns.SPACE_PATTERN.split(command.getContentDisplay(), 2)[1];
        int found = getNumberFromString(Objects.requireNonNull(message.getEmbeds().get(0).getFields().get(0).getName()));
        JsonArray array = dataFetcher.getDocsResults(query);
        if (emote.getAsCodepoints().equals(ARROW_LEFT)) {
            if (found < 10) {
                return;
            }
            scrollCommand(message, array, found - 10);
            message.removeReaction(ARROW_LEFT, reactor).queue();
        } else if (emote.getAsCodepoints().equals(ARROW_RIGHT)) {
            if (found >= array.size() - 10) {
                return;
            }
            scrollCommand(message, array, found + 10);
            message.removeReaction(ARROW_RIGHT, reactor).queue();
        } else {
            String codepoint = emote.getAsCodepoints();
            if (codepoint.length() != 16) {
                return;
            }
            int foundNumb = codepoint.charAt(3) - '0';
            if (foundNumb >= 0 && foundNumb <= Math.min(array.size() - found, 9)) {
                MessageUtils.sendEmbedWithReaction((TextChannel) message.getChannel(),
                        getResponse(array, command.getId(), found + foundNumb));
                message.delete().queue();
            }
        }
    }

    private void scrollCommand(Message message, JsonArray array, int index) {
        EmbedBuilder builder = new EmbedBuilder().setFooter(FOOTER).setColor(Color.GREEN);
        addDocsResponse(builder, array, index);
        message.editMessage(builder.build()).queue();
    }
}
