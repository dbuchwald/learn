package net.dbuchwald.learn.junit;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by dawidbuchwald on 15.02.2017.
 */
@RunWith(JUnitParamsRunner.class)
public class StringReverseServiceTest {

    @SuppressWarnings("unused")
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

    @Test
    @Parameters(method = "getTestData")
    public void stringReverseShouldReturnReversedStrings(String s, String reversed_s) {
        assertEquals("Reversed string is not equal", reversed_s, StringReverseService.reverse(s));
    }
}
