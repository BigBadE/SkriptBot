package software.bigbade.skriptbot.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Map;

class JsonKeysTest {
    @Test
    void testJsonKeysHaveRightValues() {
        for(JsonKeys keys : JsonKeys.values()) {
            Assertions.assertEquals(keys.getKey().getKey(), keys.name().toLowerCase());
            if(keys.getKey().getValue() != null) {
                Object value = keys.getKey().getValue();
                //All non null values should be 0
                if(value instanceof Map) {
                    Assertions.assertTrue(((Map<?, ?>) value).isEmpty());
                } else if(value instanceof Boolean) {
                    Assertions.assertFalse((Boolean) value);
                } else if(value instanceof Number){
                    Assertions.assertEquals(0, ((Number) keys.getKey().getValue()).intValue());
                } else {
                    throw new IllegalStateException("Object " + value + " isn't a primitive wrapper, or null!");
                }
            }
        }
    }
}
