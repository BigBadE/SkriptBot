package software.bigbade.skriptbot.testutils;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ApplicationInfo;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.StoreChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.hooks.IEventManager;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.managers.DirectAudioController;
import net.dv8tion.jda.api.managers.Presence;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.GuildAction;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.CacheView;
import net.dv8tion.jda.api.utils.cache.SnowflakeCacheView;
import okhttp3.OkHttpClient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

public class TestJDA implements JDA {
    private static final String ERROR_TEXT = "Unimplemented test method!";

    private static final SelfUser TEST_SELF_USER = new TestSelfUser("TestSelfBot");
    @Nonnull
    @Override
    public Status getStatus() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public EnumSet<GatewayIntent> getGatewayIntents() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean unloadUser(long userId) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public long getGatewayPing() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public JDA awaitStatus(@Nonnull Status status, @Nonnull Status... failOn) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public int cancelRequests() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public ScheduledExecutorService getRateLimitPool() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public ScheduledExecutorService getGatewayPool() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public ExecutorService getCallbackPool() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public OkHttpClient getHttpClient() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public DirectAudioController getDirectAudioController() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public void setEventManager(@Nullable IEventManager manager) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public void addEventListener(@Nonnull Object... listeners) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public void removeEventListener(@Nonnull Object... listeners) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public List<Object> getRegisteredListeners() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public GuildAction createGuild(@Nonnull String name) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public CacheView<AudioManager> getAudioManagerCache() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public SnowflakeCacheView<User> getUserCache() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public List<Guild> getMutualGuilds(@Nonnull User... users) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public List<Guild> getMutualGuilds(@Nonnull Collection<User> users) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public RestAction<User> retrieveUserById(long id, boolean update) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public SnowflakeCacheView<Guild> getGuildCache() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public Set<String> getUnavailableGuilds() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean isUnavailable(long guildId) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public SnowflakeCacheView<Role> getRoleCache() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public SnowflakeCacheView<Category> getCategoryCache() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public SnowflakeCacheView<StoreChannel> getStoreChannelCache() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public SnowflakeCacheView<TextChannel> getTextChannelCache() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public SnowflakeCacheView<VoiceChannel> getVoiceChannelCache() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public SnowflakeCacheView<PrivateChannel> getPrivateChannelCache() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public RestAction<PrivateChannel> openPrivateChannelById(long userId) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public SnowflakeCacheView<Emote> getEmoteCache() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public IEventManager getEventManager() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public SelfUser getSelfUser() {
        return TEST_SELF_USER;
    }

    @Nonnull
    @Override
    public Presence getPresence() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public ShardInfo getShardInfo() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public String getToken() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public long getResponseTotal() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public int getMaxReconnectDelay() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public void setAutoReconnect(boolean reconnect) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public void setRequestTimeoutRetry(boolean retryOnTimeout) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean isAutoReconnect() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean isBulkDeleteSplittingEnabled() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public void shutdown() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public void shutdownNow() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public AccountType getAccountType() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public RestAction<ApplicationInfo> retrieveApplicationInfo() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public String getInviteUrl(@Nullable Permission... permissions) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public String getInviteUrl(@Nullable Collection<Permission> permissions) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nullable
    @Override
    public ShardManager getShardManager() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public RestAction<Webhook> retrieveWebhookById(@Nonnull String webhookId) {
        throw new IllegalStateException(ERROR_TEXT);
    }
}
