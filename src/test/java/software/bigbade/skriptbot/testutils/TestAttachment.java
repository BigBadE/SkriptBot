package software.bigbade.skriptbot.testutils;

import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.Message;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

public class TestAttachment extends Message.Attachment {
    private final InputStream attachment;

    public TestAttachment(String fileName, int size, InputStream attachment) {
        super(TestIDHandler.getId(), "", "", fileName, size, 0, 0, null);
        this.attachment = attachment;
    }

    @SneakyThrows
    @Nonnull
    @Override
    public CompletableFuture<InputStream> retrieveInputStream() {
        attachment.reset();
        return CompletableFuture.completedFuture(attachment);
    }
}
