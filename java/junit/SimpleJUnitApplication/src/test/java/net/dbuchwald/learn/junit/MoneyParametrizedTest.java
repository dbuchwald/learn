package net.dbuchwald.learn.junit;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by dawidbuchwald on 14.02.2017.
 */
public class MoneyParametrizedTest {

    private static Object[] getMoney() {
        return new Object[] {
                new Object[] {10, "USD"},
                new Object[] {20, "EUR"}
        };
    }

    @ParameterizedTest
    @MethodSource("getMoney")
    public void constructorShouldSetAmountAndCurrency(int amount, String currency) {
        Money money = new Money(amount, currency);

        assertEquals(amount, money.getAmount());
        assertEquals(currency, money.getCurrency());
    }
}
