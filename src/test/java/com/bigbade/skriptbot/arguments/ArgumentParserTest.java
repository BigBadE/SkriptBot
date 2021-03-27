package com.bigbade.skriptbot.arguments;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArgumentParserTest {
    private static final String[] BASIC_ARGUMENT = new String[] { "--token", "token", "--debug", "--skunitykey", "key",
            "--pastebinKey", "pasteKey", "--prefix", "test"};
    private static final String[] DEFAULT_VALUE_ARGUMENT = new String[] { "--token", "token" };
    private static final String UNKNOWN_VALUE_ARGUMENT = "test";
    private static final String NO_VALUE_ARGUMENT = "--token";

    @Test
    @DisplayName("Parses arguments correctly")
    void testParsesArgumentsCorrectly() {
        ProgramArguments arguments = new ArgumentParser(BASIC_ARGUMENT).getArguments();
        //Make sure all arguments were parsed right
        Assertions.assertEquals("token", arguments.getToken());
        Assertions.assertEquals("test", arguments.getPrefix());
        Assertions.assertEquals("key", arguments.getSkUnityKey());
        Assertions.assertEquals("pasteKey", arguments.getPastebinKey());
        Assertions.assertTrue(arguments.isDebugMode());
        arguments = new ArgumentParser(DEFAULT_VALUE_ARGUMENT).getArguments();
        //Test default cases
        Assertions.assertEquals(".", arguments.getPrefix());
        Assertions.assertFalse(arguments.isDebugMode());
    }

    @Test
    @DisplayName("Exceptions thrown correctly")
    void testExceptionsThrownCorrectly() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ArgumentParser(new String[] { NO_VALUE_ARGUMENT }), "Argument " + NO_VALUE_ARGUMENT + " has no value");
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ArgumentParser(new String[] { UNKNOWN_VALUE_ARGUMENT }), "Unknown argument: " + UNKNOWN_VALUE_ARGUMENT);
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ArgumentParser(new String[0]), "Empty token");
    }
}
