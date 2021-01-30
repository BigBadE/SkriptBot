package software.bigbade.skriptbot.testutils;

import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.managers.AccountManager;

import javax.annotation.Nonnull;

public class TestSelfUser extends TestUser implements SelfUser {
    private static final String ERROR_TEXT = "Unimplemented test method!";

    public TestSelfUser(String name) {
        super(name);
    }

    @Override
    public boolean isVerified() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean isMfaEnabled() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public long getAllowedFileSize() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public AccountManager getManager() {
        throw new IllegalStateException(ERROR_TEXT);
    }
}
