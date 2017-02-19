package net.dbuchwald.learn.junit;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by dawidbuchwald on 16.02.2017.
 */
@RunWith(JUnitParamsRunner.class)
public class RegexDigitParserTest {

    private RegexDigitParser regexDigitParser;

    private static final int NUMBER_OF_CONSECUTIVE_DIGITS = 3;

    @SuppressWarnings("unused")
    private static Object[] getSampleCases() {
            return new Object[] {
                    new Object[] { "abc 12", "" },
                    new Object[] { "cdefg 345 12bb23", "345"},
                    new Object[] { "cdefg 345 12bbb33 678tt", "345, 678"}
            };
    }

    @Before
    public void setUp() {
        regexDigitParser = new RegexDigitParser(NUMBER_OF_CONSECUTIVE_DIGITS);
    }

    @Test
    public void constructorShouldSetNumberOfDigits() {
        assertEquals(NUMBER_OF_CONSECUTIVE_DIGITS, regexDigitParser.getNumberOfConsecutiveDigits());
    }

    @Test
    @Parameters(method = "getSampleCases")
    public void regexParserShouldExtractConsecutiveDigits(String input, String output) {
        assertEquals("Incorrect result received", output, regexDigitParser.parse(input));
    }
}
