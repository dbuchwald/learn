package net.dbuchwald.learn.junit;

/**
 * Created by dawidbuchwald on 15.02.2017.
 */
public class FahrenheitCelsiusConverter {
    public static int toFahrenheit(int i) {
        return (i*9)/5+32;
    }

    public static int toCelsius(int i) {
        return ((i-32)*5)/9;
    }
}
