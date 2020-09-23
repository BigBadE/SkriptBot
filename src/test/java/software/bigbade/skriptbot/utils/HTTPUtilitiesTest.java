package software.bigbade.skriptbot.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.bigbade.skriptbot.URLConnectionHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class HTTPUtilitiesTest {
    private static final String TEST_STRING = "testing";

    public static final Map<String, String> ARGUMENTS = new HashMap<>();
    public static final String ARGUMENT_STRING = "testing=value&second=string";
    public static final String INPUT_RETURN = "input";

    @BeforeAll
    public static void setupTests() {
        ARGUMENTS.put("testing", "value");
        ARGUMENTS.put("second", "string");
    }

    @Test
    void testSendPostRequest() {
        URLConnectionHandler.resetValues(INPUT_RETURN, ARGUMENT_STRING, connection -> {
            Assertions.assertEquals("application/x-www-form-urlencoded; charset=UTF-8",
                    connection.getConnection().getRequestProperties().get("Content-Type").get(0));
            Assertions.assertEquals("POST", connection.getConnection().getRequestMethod());
            Assertions.assertTrue(connection.getConnection().getDoOutput());
            Assertions.assertEquals(ARGUMENT_STRING.length(), connection.getContentLength());
        });
        Optional<InputStream> input = HTTPUtilities.sendPostRequest("testing:test.file", ARGUMENTS);
        Assertions.assertTrue(input.isPresent());
        Assertions.assertEquals(INPUT_RETURN, HTTPUtilities.readInputStream(input.get()));
    }

    @Test
    void testReadInputStream() {
        InputStream stream = new ByteArrayInputStream(TEST_STRING.getBytes(StandardCharsets.UTF_8));
        Assertions.assertEquals(TEST_STRING, HTTPUtilities.readInputStream(stream));
    }
}


