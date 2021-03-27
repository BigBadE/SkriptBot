package com.bigbade.skriptbot.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RegexPatternsTest {
    @Test
    void testRegexPatterns() {
        Assertions.assertEquals(2, RegexPatterns.SPACE_PATTERN.split("test string").length);
        Assertions.assertEquals(1, RegexPatterns.SPACE_PATTERN.split("test").length);
    }
}
