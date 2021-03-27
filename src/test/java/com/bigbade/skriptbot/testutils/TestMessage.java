package com.bigbade.skriptbot.testutils;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageActivity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.requests.restaction.pagination.ReactionPaginationAction;
import org.apache.commons.collections4.Bag;
import org.junit.jupiter.api.Assertions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Formatter;
import java.util.List;
import java.util.regex.Pattern;

public class TestMessage implements Message {
    private static final Pattern UNICODE_SPLITTER = Pattern.compile("U+");
    private static final String ERROR_TEXT = "Unimplemented test method!";

    private final String text;

    @Getter
    private final List<MessageEmbed> embeds = new ArrayList<>();

    @Getter
    private final List<Attachment> attachments = new ArrayList<>();

    @Getter
    private final TextChannel channel;

    @Setter
    @Getter
    private TestUser author;

    public boolean deleted = false;

    private int expectedReactions = 0;

    private final long id = TestIDHandler.getId();

    public TestMessage(String text, TextChannel channel) {
        this.channel = channel;
        this.text = text;
    }

    public TestMessage(MessageEmbed embed, TextChannel channel) {
        this.channel = channel;
        text = "";
        embeds.add(embed);
    }

    public static String hexToName(String hex) {
        String[] split = UNICODE_SPLITTER.split(hex);
        char[] hexes = new char[split.length-1];
        int i = 0;
        for(String found : split) {
            if(found.isEmpty()) {
                continue;
            }
            hexes[i++] = (char) Integer.parseInt(found, 16);
        }
        return new String(hexes);
    }

    public void addAttachment(Attachment attachment) { attachments.add(attachment); }

    public void verify(boolean isDeleted) {
        Assertions.assertEquals(0, expectedReactions);
        Assertions.assertEquals(isDeleted, deleted);
    }

    public static void assertEmbedsEqual(MessageEmbed expected, MessageEmbed actual) {
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getUrl(), actual.getUrl());
        for (int i = 0; i < expected.getFields().size(); i++) {
            MessageEmbed.Field first = expected.getFields().get(i);
            MessageEmbed.Field second = actual.getFields().get(i);
            Assertions.assertEquals(first.getName(), second.getName());
            Assertions.assertEquals(first.getValue(), second.getValue());
        }
    }

    @Nullable
    @Override
    public Message getReferencedMessage() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public List<User> getMentionedUsers() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public Bag<User> getMentionedUsersBag() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public List<TextChannel> getMentionedChannels() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public Bag<TextChannel> getMentionedChannelsBag() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public List<Role> getMentionedRoles() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public Bag<Role> getMentionedRolesBag() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public List<Member> getMentionedMembers(@Nonnull Guild guild) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public List<Member> getMentionedMembers() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public List<IMentionable> getMentions(@Nonnull MentionType... types) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean isMentioned(@Nonnull IMentionable mentionable, @Nonnull MentionType... types) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public List<Emote> getEmotes() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean mentionsEveryone() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean isEdited() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nullable
    @Override
    public OffsetDateTime getTimeEdited() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nullable
    @Override
    public Member getMember() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public String getJumpUrl() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public String getContentDisplay() {
        return text;
    }

    @Nonnull
    @Override
    public String getContentRaw() {
        return text;
    }

    @Nonnull
    @Override
    public String getContentStripped() {
        return text;
    }

    @Nonnull
    @Override
    public List<String> getInvites() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nullable
    @Override
    public String getNonce() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean isFromType(@Nonnull ChannelType type) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public ChannelType getChannelType() {
        return channel.getType();
    }

    @Override
    public boolean isWebhookMessage() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public PrivateChannel getPrivateChannel() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public TextChannel getTextChannel() {
        return channel;
    }

    @Nullable
    @Override
    public Category getCategory() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public Guild getGuild() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public Bag<Emote> getEmotesBag() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public List<MessageReaction> getReactions() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean isTTS() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nullable
    @Override
    public MessageActivity getActivity() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageAction editMessage(@Nonnull CharSequence newContent) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageAction editMessage(@Nonnull MessageEmbed newContent) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageAction editMessageFormat(@Nonnull String format, @Nonnull Object... args) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageAction editMessage(@Nonnull Message newContent) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public AuditableRestAction<Void> delete() {
        deleted = true;
        return new TestRestAction<>(null);
    }

    @Nonnull
    @Override
    public JDA getJDA() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean isPinned() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public RestAction<Void> pin() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public RestAction<Void> unpin() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public RestAction<Void> addReaction(@Nonnull Emote emote) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public RestAction<Void> addReaction(@Nonnull String unicode) {
        expectedReactions--;
        return new TestRestAction<>(null);
    }

    @Nonnull
    @Override
    public RestAction<Void> clearReactions() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public RestAction<Void> clearReactions(@Nonnull String unicode) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public RestAction<Void> clearReactions(@Nonnull Emote emote) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public RestAction<Void> removeReaction(@Nonnull Emote emote) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public RestAction<Void> removeReaction(@Nonnull Emote emote, @Nonnull User user) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public RestAction<Void> removeReaction(@Nonnull String unicode) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public RestAction<Void> removeReaction(@Nonnull String unicode, @Nonnull User user) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public ReactionPaginationAction retrieveReactionUsers(@Nonnull Emote emote) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public ReactionPaginationAction retrieveReactionUsers(@Nonnull String unicode) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nullable
    @Override
    public MessageReaction.ReactionEmote getReactionByUnicode(@Nonnull String unicode) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nullable
    @Override
    public MessageReaction.ReactionEmote getReactionById(@Nonnull String id) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nullable
    @Override
    public MessageReaction.ReactionEmote getReactionById(long id) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public AuditableRestAction<Void> suppressEmbeds(boolean suppressed) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public RestAction<Message> crosspost() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean isSuppressedEmbeds() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public EnumSet<MessageFlag> getFlags() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageType getType() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public void formatTo(Formatter formatter, int flags, int width, int precision) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public long getIdLong() {
        return id;
    }
}
