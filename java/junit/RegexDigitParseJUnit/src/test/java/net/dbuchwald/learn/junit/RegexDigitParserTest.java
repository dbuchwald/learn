package net.dbuchwald.learn.junit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by dawidbuchwald on 16.02.2017.
 */
public class RegexDigitParserTest {

    private RegexDigitParser regexDigitParser;

    private static final int NUMBER_OF_CONSECUTIVE_DIGITS = 3;

    private static Object[] getSampleCases() {
            return new Object[] {
                    new Object[] { "abc 12", "" },
                    new Object[] { "cdefg 345 12bb23", "345"},
                    new Object[] { "cdefg 345 12bbb33 678tt", "345, 678"}
            };
    }

    @BeforeEach
    public void setUp() {
        regexDigitParser = new RegexDigitParser(NUMBER_OF_CONSECUTIVE_DIGITS);
    }

    @Test
    public void constructorShouldSetNumberOfDigits() {
        assertEquals(NUMBER_OF_CONSECUTIVE_DIGITS, regexDigitParser.getNumberOfConsecutiveDigits());
    }

    @ParameterizedTest
    @MethodSource("getSampleCases")
    public void regexParserShouldExtractConsecutiveDigits(String input, String output) {
        assertEquals(output, regexDigitParser.parse(input), "Incorrect result received");
    }
}
