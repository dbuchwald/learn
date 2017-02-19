package net.dbuchwald.learn.junit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by dawidbuchwald on 14.02.2017.
 */
public class MoneyTest {

    @Test
    public void constructorShouldSetAmountAndCurrency() {
        Money money=new Money(10, "USD");

        assertEquals(10, money.getAmount());
        assertEquals("USD", money.getCurrency());
    }
}
