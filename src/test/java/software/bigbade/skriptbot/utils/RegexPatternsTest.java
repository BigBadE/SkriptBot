package software.bigbade.skriptbot.utils;

import org.junit.jupiter.api.Assertions;

class RegexPatternsTest {
    void testRegexPatterns() {
        Assertions.assertTrue(RegexPatterns.SPACE_PATTERN.matcher("test string").matches());
        Assertions.assertFalse(RegexPatterns.SPACE_PATTERN.matcher("test").matches());
    }
}
