package com.bigbade.skriptbot.commands;

import com.bigbade.skriptbot.DocsPage;
import com.bigbade.skriptbot.api.ICommand;
import com.bigbade.skriptbot.api.IDataFetcher;
import com.bigbade.skriptbot.utils.HTMLUtilities;
import com.bigbade.skriptbot.utils.JsonKeys;
import com.bigbade.skriptbot.utils.MessageUtils;
import com.bigbade.skriptbot.utils.RegexPatterns;
import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.Color;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class DocSearchCommand implements ICommand {
    public static final String FOOTER = "Powered by skUnity Docs 2 | ";
    @Getter
    private final List<String> aliases = Collections.unmodifiableList(Arrays.asList("d", "doc", "docs", "documentation"));
    private final IDataFetcher dataFetcher;
    private final String prefix;

    /**
     * Converts string starting with a number that ends with a '.'
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
                                + URLEncoder.encode(object.getString(JsonKeys.NAME.getKey()),
                                StandardCharsets.UTF_8.name()))
                .setFooter(FOOTER + id)
                .setColor(Color.YELLOW)
                .addField("Pattern", "```vb\n" + object.getString(JsonKeys.PATTERN.getKey()) + "```", false);
        String description = object.getString(JsonKeys.DESC.getKey());
        if (!description.isEmpty()) {
            builder.addField("Description", object.getString(JsonKeys.DESC.getKey()), false);
        }
        JsonArray examples = (JsonArray) object.get("examples");
        if (!examples.isEmpty()) {
            builder.addField("Example", "```vb\n" + HTMLUtilities.unescapeHtml(((JsonObject) examples.get(0))
                    .getString(JsonKeys.EXAMPLE.getKey())) + "```", false);
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

    /**
     * Does nothing with the input. Usually to ignore errors from queue'd messages.
     *
     * @param unused Unused param.
     * @param <T>    Type of input, usually Void or Throwable
     */
    @SuppressWarnings({"EmptyMethod", "unused"})
    public static <T> void ignoreInput(T unused) {
        //Ignore error
    }

    public static void addDocsResponse(EmbedBuilder builder, JsonArray array, int start) {
        for (int i = start; i < Math.min(array.size(), start + 10); i++) {
            JsonObject result = (JsonObject) array.get(i);
            String doc = result.getString(JsonKeys.DOC.getKey());
            builder.addField(i + ". " + result.getString(JsonKeys.NAME.getKey()) + " ("
                            + doc.substring(0, doc.length() - 1) + ")",
                    "```vb\n" + result.getString(JsonKeys.PATTERN.getKey()) + "```", false);
        }
        if (array.size() > start + 10) {
            builder.addField("And " + (array.size() - start - 10) + " more", "", false);
        }
    }

    private static void scrollCommand(Message message, MessageEmbed embed, JsonArray array, int index) {
        @SuppressWarnings("ConstantConditions")
        EmbedBuilder builder = new EmbedBuilder().setTitle(embed.getTitle(), embed.getUrl())
                .setFooter(embed.getFooter().getText()).setColor(Color.GREEN);
        addDocsResponse(builder, array, index);
        message.editMessage(builder.build()).queue();
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    @Override
    public void onCommand(Message message, String[] args) {
        if (args.length == 0) {
            MessageUtils.sendEmbedWithReaction(message.getTextChannel(), MessageUtils.getErrorMessage(message.getId(),
                    "No Syntax Specified",
                    "Usage: **" + prefix + "doc subtext**"));
            return;
        }

        String query = String.join(" ", args);
        JsonArray array = dataFetcher.getDocsResults(query);
        if (array.isEmpty()) {
            MessageUtils.sendEmbedWithReaction(message.getTextChannel(), MessageUtils.getErrorMessage(message.getId(),
                    "No Results",
                    "No results were found for that query"));
            return;
        }
        if (array.size() == 1) {
            MessageUtils.sendEmbedWithReaction(message.getTextChannel(), getResponse(array, message.getId(), 0));
            return;
        }
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setFooter(FOOTER + message.getId())
                .setTitle("Found Results", "https://docs.skunity.com/syntax/search/"
                        + URLEncoder.encode(query, StandardCharsets.UTF_8.name()));
        addDocsResponse(builder, array, 0);
        DocsPage page = new DocsPage(0, array.size());
        message.getTextChannel().sendMessage(builder.build()).queue(page::addReactions);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onReaction(User reactor, Message command, Message message, MessageReaction.ReactionEmote emote) {
        String query = RegexPatterns.SPACE_PATTERN.split(command.getContentDisplay(), 2)[1];
        MessageEmbed embed = message.getEmbeds().get(0);
        if (!embed.getTitle().equals("Found Results")) {
            return;
        }
        int found = getNumberFromString(embed.getFields().get(0).getName());
        JsonArray array = dataFetcher.getDocsResults(query);
        DocsPage page = new DocsPage(found, array.size());
        page.scroll(message, reactor, emote.getAsCodepoints());
        if (page.isLeft(emote.getAsCodepoints())) {
            scrollCommand(message, embed, array, found - 10);
        } else if (page.isRight(emote.getAsCodepoints())) {
            scrollCommand(message, embed, array, found + 10);
        } else {
            String codepoint = emote.getAsCodepoints();
            if (codepoint.length() < 16) {
                return;
            }
            int foundNumb = codepoint.charAt(3) - '0';
            if (foundNumb >= 0 && foundNumb <= array.size() - found) {
                if(found + foundNumb >= array.size()) {
                    return;
                }
                MessageUtils.sendEmbedWithReaction((TextChannel) message.getChannel(),
                        getResponse(array, command.getId(), found + foundNumb));
            }
        }
    }
}
