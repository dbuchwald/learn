package net.dbuchwald.learn.junit;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import net.dbuchwald.learn.junit.FahrenheitCelsiusConverter;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by dawidbuchwald on 15.02.2017.
 */
@RunWith(JUnitParamsRunner.class)
public class FahrenheitCelsiusConverterTest {

    @SuppressWarnings("unused")
    private static Object[] getSampleDataC2F() {
        return new Object[] {
                new Object[] { 0, 32 },
                new Object[] { 37, 98 },
                new Object[] { 100, 212 }
        };
    }

    @SuppressWarnings("unused")
    private static Object[] getSampleDataF2C() {
        return new Object[] {
                new Object[] { 32, 0 },
                new Object[] { 100, 37 },
                new Object[] { 212, 100 }
        };
    }

    @Test
    public void shouldConvertCelsiusToFahrenheit() {
        assertEquals(32, FahrenheitCelsiusConverter.toFahrenheit(0));
        assertEquals(98, FahrenheitCelsiusConverter.toFahrenheit(37));
        assertEquals(212, FahrenheitCelsiusConverter.toFahrenheit(100));
    }

    @Test
    public void shouldConvertFahrenheitToCelsius() {
        assertEquals(0, FahrenheitCelsiusConverter.toCelsius(32));
        assertEquals(37, FahrenheitCelsiusConverter.toCelsius(100));
        assertEquals(100, FahrenheitCelsiusConverter.toCelsius(212));
    }

    @Test
    @Parameters(method = "getSampleDataC2F")
    public void shouldConvertCelsiusToFahrenheit(int c, int f) {
        assertEquals("Converted value is not correct", f, FahrenheitCelsiusConverter.toFahrenheit(c));
    }

    @Test
    @Parameters(method = "getSampleDataF2C")
    public void shouldConvertFahrenheitToCelsius(int f, int c) {
        assertEquals("Converted value is not correct", c, FahrenheitCelsiusConverter.toCelsius(f));
    }
}
