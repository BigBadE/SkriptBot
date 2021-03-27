package com.bigbade.skriptbot.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HTMLUtilitiesTest {
    private static final String TEST_STRING = "&test;testingString";

    @Test
    void testHTMLUnescaper() {
        Assertions.assertEquals("\"<>", HTMLUtilities.unescapeHtml("&quot;&gt;&lt;"));
        Assertions.assertEquals(TEST_STRING, HTMLUtilities.unescapeHtml(TEST_STRING));
    }
}
