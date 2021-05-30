package com.bigbade.skriptbot.testutils;

import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.IPermissionHolder;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.managers.ChannelManager;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import net.dv8tion.jda.api.requests.restaction.InviteAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.requests.restaction.PermissionOverrideAction;
import net.dv8tion.jda.api.requests.restaction.WebhookAction;
import org.junit.jupiter.api.Assertions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A Channel instance used for testing. All unimplemented test methods throw an IllegalStateException
 * to prevent false positives due to incorrect return values.
 */
public class TestChannel implements TextChannel {
    private static final String ERROR_TEXT = "Unimplemented test method!";

    private final List<TestMessage> expectedMessages = new ArrayList<>();
    private final long id = TestIDHandler.getId();

    @Setter
    private ChannelType type = ChannelType.TEXT;

    @Setter
    private Message retrievedMessage = null;

    @Nonnull
    @Override
    public MessageAction sendMessage(@Nonnull MessageEmbed embed) {
        System.out.println(expectedMessages.get(expectedMessages.size()-1).getEmbeds().get(0).getTitle() + " vs " + embed.getTitle());
        TestMessage.assertEmbedsEqual(expectedMessages.get(expectedMessages.size()-1).getEmbeds().get(0), embed);
        expectedMessages.remove(expectedMessages.size()-1);
        return new TestMessageAction(embed, this);
    }

    public void expectMessage(TestMessage message) {
        expectedMessages.add(message);
    }

    public void expectMessage(MessageEmbed embed) { expectedMessages.add(new TestMessage(embed, this)); }

    public void verify() {
        Assertions.assertTrue(expectedMessages.isEmpty());
    }

    @Nonnull
    @Override
    public RestAction<Message> retrieveMessageById(@Nonnull String messageId) {
        Message retrieved = retrievedMessage;
        retrievedMessage = null;
        return new TestRestAction<>(retrieved);
    }

    @Nullable
    @Override
    public String getTopic() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean isNSFW() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean isNews() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public int getSlowmode() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public ChannelType getType() {
        return type;
    }

    @Override
    public long getLatestMessageIdLong() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean hasLatestMessage() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public String getName() {
        return "TestChannel";
    }

    @Nonnull
    @Override
    public Guild getGuild() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nullable
    @Override
    public Category getParent() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public List<Member> getMembers() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public int getPosition() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public int getPositionRaw() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public JDA getJDA() {
        return TestJDA.TEST_JDA;
    }

    @Nullable
    @Override
    public PermissionOverride getPermissionOverride(@Nonnull IPermissionHolder permissionHolder) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public List<PermissionOverride> getPermissionOverrides() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public List<PermissionOverride> getMemberPermissionOverrides() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public List<PermissionOverride> getRolePermissionOverrides() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean isSynced() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public ChannelAction<TextChannel> createCopy(@Nonnull Guild guild) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public ChannelAction<TextChannel> createCopy() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public ChannelManager getManager() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public AuditableRestAction<Void> delete() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public PermissionOverrideAction createPermissionOverride(@Nonnull IPermissionHolder permissionHolder) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public PermissionOverrideAction putPermissionOverride(@Nonnull IPermissionHolder permissionHolder) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public InviteAction createInvite() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public RestAction<List<Invite>> retrieveInvites() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public RestAction<List<Webhook>> retrieveWebhooks() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public WebhookAction createWebhook(@Nonnull String name) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public RestAction<Webhook.WebhookReference> follow(@Nonnull String targetChannelId) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public RestAction<Void> deleteMessages(@Nonnull Collection<Message> messages) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public RestAction<Void> deleteMessagesByIds(@Nonnull Collection<String> messageIds) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public AuditableRestAction<Void> deleteWebhookById(@Nonnull String id) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public RestAction<Void> clearReactionsById(@Nonnull String messageId) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public RestAction<Void> clearReactionsById(@Nonnull String messageId, @Nonnull String unicode) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public RestAction<Void> clearReactionsById(@Nonnull String messageId, @Nonnull Emote emote) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public RestAction<Void> removeReactionById(@Nonnull String messageId, @Nonnull String unicode, @Nonnull User user) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean canTalk() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean canTalk(@Nonnull Member member) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public int compareTo(@Nonnull GuildChannel channel) {
        return Long.compareUnsigned(channel.getIdLong(), getIdLong());
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof GuildChannel)) {
            return false;
        }
        return compareTo((GuildChannel) obj) == 0;
    }

    @Override
    public int hashCode() {
        return (int) getIdLong();
    }

    @Nonnull
    @Override
    public String getAsMention() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public long getIdLong() {
        return id;
    }
}
