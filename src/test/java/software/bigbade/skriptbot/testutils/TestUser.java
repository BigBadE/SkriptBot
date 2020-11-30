package software.bigbade.skriptbot.testutils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class TestUser implements User {
    private static final String ERROR_TEXT = "Unimplemented test method!";

    @Nonnull
    @Override
    public String getName() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public String getDiscriminator() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nullable
    @Override
    public String getAvatarId() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public String getDefaultAvatarId() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public String getAsTag() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean hasPrivateChannel() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public RestAction<PrivateChannel> openPrivateChannel() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public List<Guild> getMutualGuilds() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean isBot() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public JDA getJDA() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public EnumSet<UserFlag> getFlags() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public int getFlagsRaw() {
        throw new IllegalStateException(ERROR_TEXT);
    }

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
        throw new IllegalStateException(ERROR_TEXT);
    }
}
