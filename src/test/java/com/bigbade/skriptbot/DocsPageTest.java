package com.bigbade.skriptbot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

//Other methods are tested by DocSearchCommandTest
class DocsPageTest {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void testGetNumberFromEmote() {
        Assertions.assertEquals("U+37U+fe0fU+20e3", DocsPage.getNumberEmote(7));
        Assertions.assertThrows(IllegalArgumentException.class, () -> DocsPage.getNumberEmote(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> DocsPage.getNumberEmote(11));
    }
}
