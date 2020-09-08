package software.bigbade.skriptbot.command;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import software.bigbade.skriptbot.commands.AddonSearchCommand;

class AddonSearchCommandTest {
    @Test
    void testHumanReadableByteContentSI() {
        Assertions.assertEquals("12 B", AddonSearchCommand.humanReadableByteCountSI(12));
        Assertions.assertEquals("1.0 kB", AddonSearchCommand.humanReadableByteCountSI(1000));
        Assertions.assertEquals("1.9 kB", AddonSearchCommand.humanReadableByteCountSI(1949));
        Assertions.assertEquals("1.0 MB", AddonSearchCommand.humanReadableByteCountSI(1000000));
        Assertions.assertEquals("1.9 GB", AddonSearchCommand.humanReadableByteCountSI(1940000000));
    }
}
