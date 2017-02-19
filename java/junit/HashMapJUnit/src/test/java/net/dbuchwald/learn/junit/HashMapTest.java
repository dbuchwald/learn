package net.dbuchwald.learn.junit;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by dawidbuchwald on 15.02.2017.
 */
@RunWith(JUnitParamsRunner.class)
public class HashMapTest {

    private Map<String, String> keyValueMap;

    @SuppressWarnings("unused")
    private static Object[] getSampleEntriesGetPut() {
        return new Object[] {
               new Object[] { "1", "One" },
               new Object[] { "2", "Two" },
               new Object[] { "3", "Three" }
        };
    }

    @SuppressWarnings("unused")
    private static Object[] getSampleEntriesPutPut() {
        return new Object[] {
               new Object[] { "1", "One", "Ein" },
               new Object[] { "2", "Two", "Zwei" },
               new Object[] { "3", "Three", "Drei" }
        };
    }

    @Before
    public void setUp() {
        keyValueMap = new HashMap<>();
    }

    @Test
    @Parameters(method = "getSampleEntriesGetPut")
    public void entriesStoredWithPutShouldBeRetrievedWithGet(String key, String value) {
        keyValueMap.put(key, value);
        //assertEquals("Stored value does not match parameter", value, );
        String retrievedValue = keyValueMap.get(key);
        assertNotNull("Value for given key does not exist", retrievedValue);
        assertEquals("Value retrieved for given key is not correct", value, retrievedValue);
    }

    @Test
    @Parameters(method = "getSampleEntriesPutPut")
    public void entriesShouldBeOverwrittenOnDuplicatePut(String key, String firstValue, String secondValue) {
        keyValueMap.put(key, firstValue);
        String previousValue = keyValueMap.put(key, secondValue);
        assertEquals("Previous value is incorrect", firstValue, previousValue);

        assertEquals("Incorrect value retrieved", secondValue, keyValueMap.get(key));
    }

    @Test
    public void clearMethodShouldEmptyMap() {
        keyValueMap.put("1", "One");
        keyValueMap.put("2", "Two");

        assertEquals("Two entries are expected in map", 2, keyValueMap.size());
        keyValueMap.clear();
        assertEquals("No entries are expected in map after clear", 0, keyValueMap.size());
    }

    @Test
    public void nullKeyShouldBeAccepted() {
        keyValueMap.put(null, "null");

        assertEquals("Map should contain single entry", 1, keyValueMap.size());
        assertNotNull("It should be possible to retrieve value indexed by null", keyValueMap.get(null));
        assertEquals("Value retrieved for null key should be correct", "null", keyValueMap.get(null));
    }
}
