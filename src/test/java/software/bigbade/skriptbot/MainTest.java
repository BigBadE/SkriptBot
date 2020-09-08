package software.bigbade.skriptbot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MainTest {
    @Test
    void testInvalidToken() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Main.main(new String[] { "--token", "notatoken" }));
    }
}
