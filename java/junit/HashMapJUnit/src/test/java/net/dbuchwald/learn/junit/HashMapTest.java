package net.dbuchwald.learn.junit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by dawidbuchwald on 15.02.2017.
 */
public class HashMapTest {

    private Map<String, String> keyValueMap;

    private static Object[] getSampleEntriesGetPut() {
        return new Object[] {
               new Object[] { "1", "One" },
               new Object[] { "2", "Two" },
               new Object[] { "3", "Three" }
        };
    }

    private static Object[] getSampleEntriesPutPut() {
        return new Object[] {
               new Object[] { "1", "One", "Ein" },
               new Object[] { "2", "Two", "Zwei" },
               new Object[] { "3", "Three", "Drei" }
        };
    }

    @BeforeEach
    public void setUp() {
        keyValueMap = new HashMap<>();
    }

    @ParameterizedTest
    @MethodSource("getSampleEntriesGetPut")
    public void entriesStoredWithPutShouldBeRetrievedWithGet(String key, String value) {
        keyValueMap.put(key, value);
        //assertEquals("Stored value does not match parameter", value, );
        String retrievedValue = keyValueMap.get(key);
        assertNotNull(retrievedValue, "Value for given key does not exist");
        assertEquals(value, retrievedValue, "Value retrieved for given key is not correct");
    }

    @ParameterizedTest
    @MethodSource("getSampleEntriesPutPut")
    public void entriesShouldBeOverwrittenOnDuplicatePut(String key, String firstValue, String secondValue) {
        keyValueMap.put(key, firstValue);
        String previousValue = keyValueMap.put(key, secondValue);
        assertEquals(firstValue, previousValue, "Previous value is incorrect");

        assertEquals(secondValue, keyValueMap.get(key), "Incorrect value retrieved");
    }

    @Test
    public void clearMethodShouldEmptyMap() {
        keyValueMap.put("1", "One");
        keyValueMap.put("2", "Two");

        assertEquals(2, keyValueMap.size(), "Two entries are expected in map");
        keyValueMap.clear();
        assertEquals(0, keyValueMap.size(), "No entries are expected in map after clear");
    }

    @Test
    public void nullKeyShouldBeAccepted() {
        keyValueMap.put(null, "null");

        assertEquals(1, keyValueMap.size(), "Map should contain single entry");
        assertNotNull(keyValueMap.get(null), "It should be possible to retrieve value indexed by null");
        assertEquals("null", keyValueMap.get(null), "Value retrieved for null key should be correct");
    }
}
