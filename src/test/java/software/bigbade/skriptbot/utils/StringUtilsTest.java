package software.bigbade.skriptbot.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringUtilsTest {
    @Test
    void testMatchesArray() {
        Assertions.assertTrue(StringUtils.matchesArray("test", new String[] { "test" }));
        Assertions.assertFalse(StringUtils.matchesArray("Test", new String[] { "test" }));
    }
}
