package software.bigbade.skriptbot;

import lombok.Getter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class URLConnectionHandler extends URLStreamHandler {
    private String inputStream;
    private String outputStream;
    private Consumer<URLConnectionHandler> checker;

    private static final URLConnectionHandler handler = new URLConnectionHandler();

    private static boolean registered;

    @Getter
    private int contentLength;

    @Getter
    public final HttpURLConnection connection = new HttpURLConnection(null) {
        @Override
        public void disconnect() { }

        @Override
        public boolean usingProxy() {
            return false;
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(inputStream.getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public OutputStream getOutputStream() {
            return new OutputStream() {
                @Override
                public void write(int b) {
                    throw new IllegalStateException("Should not be called!");
                }

                @Override
                public void write(@Nonnull byte[] b) {
                    if(outputStream == null) {
                        Assertions.fail();
                    } else {
                        Assertions.assertArrayEquals(outputStream.getBytes(StandardCharsets.UTF_8), b);
                    }
                }
            };
        }

        @SneakyThrows
        @Override
        public void connect() {
            contentLength = this.fixedContentLength;
            if(checker != null) {
                checker.accept(handler);
            }
        }
    };

    @Override
    protected URLConnection openConnection(URL u) {
        return connection;
    }

    public static void resetValues(@Nonnull String input, @Nullable String output, @Nullable Consumer<URLConnectionHandler> checker) {
        if(!registered) {
            URL.setURLStreamHandlerFactory(protocol -> URLConnectionHandler.handler);
            registered = true;
        }
        URLConnectionHandler.handler.inputStream = input;
        URLConnectionHandler.handler.outputStream = output;
        URLConnectionHandler.handler.checker = checker;
    }
}
