package software.bigbade.skriptbot.testutils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.utils.AttachmentOption;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;


public class TestMessageAction implements MessageAction {
    private static final String ERROR_TEXT = "Unimplemented test method!";

    private final TestMessage message;

    public TestMessageAction(MessageEmbed embed, TestChannel channel) {
        message = new TestMessage(embed, channel);
    }

    @Nonnull
    @Override
    public JDA getJDA() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageAction setCheck(@Nullable BooleanSupplier checks) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageAction timeout(long timeout, @Nonnull TimeUnit unit) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageAction deadline(long timestamp) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public void queue(@Nullable Consumer<? super Message> success, @Nullable Consumer<? super Throwable> failure) {
        if(success != null) {
            success.accept(message);
        }
    }

    @Override
    public Message complete(boolean shouldQueue) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public CompletableFuture<Message> submit(boolean shouldQueue) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageChannel getChannel() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean isEmpty() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public boolean isEdit() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageAction apply(@Nullable Message message) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageAction tts(boolean isTTS) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageAction reset() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageAction nonce(@Nullable String nonce) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageAction content(@Nullable String content) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageAction embed(@Nullable MessageEmbed embed) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageAction append(@Nullable CharSequence csq, int start, int end) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageAction append(char c) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageAction addFile(@Nonnull InputStream data, @Nonnull String name, @Nonnull AttachmentOption... options) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageAction addFile(@Nonnull File file, @Nonnull String name, @Nonnull AttachmentOption... options) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageAction clearFiles() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageAction clearFiles(@Nonnull BiConsumer<String, InputStream> finalizer) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageAction clearFiles(@Nonnull Consumer<InputStream> finalizer) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageAction override(boolean bool) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageAction allowedMentions(@Nullable Collection<Message.MentionType> allowedMentions) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageAction mention(@Nonnull IMentionable... mentions) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageAction mentionUsers(@Nonnull String... userIds) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public MessageAction mentionRoles(@Nonnull String... roleIds) {
        throw new IllegalStateException(ERROR_TEXT);
    }
}
