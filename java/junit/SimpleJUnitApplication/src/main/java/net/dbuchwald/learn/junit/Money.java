package net.dbuchwald.learn.junit;

/**
 * Created by dawidbuchwald on 14.02.2017.
 */
public class Money {
    private final int amount;
    private final String currency;

    public Money(int amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public int getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }


    public boolean equals(Object object) {
        if (object instanceof Money) {
            Money money = (Money) object;
            return (money.getAmount() == this.getAmount() &&
                    money.getCurrency().equals(this.getCurrency()));
        } else
            return false;
    }
}
