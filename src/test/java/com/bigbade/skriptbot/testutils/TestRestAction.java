package com.bigbade.skriptbot.testutils;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class TestRestAction<T> implements AuditableRestAction<T> {
    private static final String ERROR_TEXT = "Unimplemented test method!";

    private final T value;

    @Nonnull
    @Override
    public JDA getJDA() {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public AuditableRestAction<T> reason(@Nullable String reason) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public AuditableRestAction<T> setCheck(@Nullable BooleanSupplier checks) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public AuditableRestAction<T> timeout(long timeout, @Nonnull TimeUnit unit) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public AuditableRestAction<T> deadline(long timestamp) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Override
    public void queue(@Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> failure) {
        if(success != null) {
            success.accept(value);
        }
    }

    @Override
    public T complete(boolean shouldQueue) {
        throw new IllegalStateException(ERROR_TEXT);
    }

    @Nonnull
    @Override
    public CompletableFuture<T> submit(boolean shouldQueue) {
        throw new IllegalStateException(ERROR_TEXT);
    }
}
