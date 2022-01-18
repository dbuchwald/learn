package net.dbuchwald.learn.junit;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by dawidbuchwald on 15.02.2017.
 */
public class StringReverseServiceTest {

    private static Object[] getTestData() {
        return new Object[] {
                new Object[] { "a", "a" },
                new Object[] { "Test", "tseT" },
                new Object[] { "AAA", "AAA" },
                new Object[] { "", "" },
                new Object[] { "Long test", "tset gnoL" },
                new Object[] { null, null }
        };
    }

    @ParameterizedTest
    @MethodSource("getTestData")
    public void stringReverseShouldReturnReversedStrings(String s, String reversed_s) {
        assertEquals(reversed_s, StringReverseService.reverse(s), "Reversed string is not equal");
    }
}
