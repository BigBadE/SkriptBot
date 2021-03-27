package com.bigbade.skriptbot.testutils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ClientType;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Color;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

public class TestMember implements Member {
    private static final String ERROR_TEXT = "Unimplemented test method!";

    private final TestUser user;

    public TestMember(TestUser user) {
        this.user = user;
    }

    @Nonnull
    @Override
    public User getUser() {
        return user;
    }

    @Nonnull
    @Override
    public Guild getGuild() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public EnumSet<Permission> getPermissions() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public EnumSet<Permission> getPermissions(@Nonnull GuildChannel channel) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public EnumSet<Permission> getPermissionsExplicit() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public EnumSet<Permission> getPermissionsExplicit(@Nonnull GuildChannel channel) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean hasPermission(@Nonnull Permission... permissions) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean hasPermission(@Nonnull Collection<Permission> permissions) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean hasPermission(@Nonnull GuildChannel channel, @Nonnull Permission... permissions) {
        return hasPermission(permissions);
    }

    @Override
    public boolean hasPermission(@Nonnull GuildChannel channel, @Nonnull Collection<Permission> permissions) {
        return hasPermission(permissions);
    }

    @Override
    public boolean canSync(@Nonnull GuildChannel targetChannel, @Nonnull GuildChannel syncSource) {
        return false;
    }

    @Override
    public boolean canSync(@Nonnull GuildChannel channel) {
        return false;
    }

    @Nonnull
    @Override
    public JDA getJDA() {
        return TestJDA.TEST_JDA;
    }

    @Nonnull
    @Override
    public OffsetDateTime getTimeJoined() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean hasTimeJoined() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nullable
    @Override
    public OffsetDateTime getTimeBoosted() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nullable
    @Override
    public GuildVoiceState getVoiceState() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public List<Activity> getActivities() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public OnlineStatus getOnlineStatus() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public OnlineStatus getOnlineStatus(@Nonnull ClientType type) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public EnumSet<ClientType> getActiveClients() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nullable
    @Override
    public String getNickname() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public String getEffectiveName() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public List<Role> getRoles() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nullable
    @Override
    public Color getColor() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public int getColorRaw() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean canInteract(@Nonnull Member member) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean canInteract(@Nonnull Role role) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean canInteract(@Nonnull Emote emote) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean isOwner() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean isPending() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nullable
    @Override
    public TextChannel getDefaultChannel() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public boolean isFake() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public String getAsMention() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public long getIdLong() {
        return user.getIdLong();
    }
}
