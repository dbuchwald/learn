package net.dbuchwald.learn.junit;

import java.util.Arrays;

/**
 * Created by dawidbuchwald on 14.02.2017.
 */
public class PhoneNumber {

    private final String countryCode;
    private final String prefix;
    private final String number;

    public String getCountryCode() {
        return countryCode;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getNumber() {
        return number;
    }

    public PhoneNumber(String country_code, String prefix, String number) {
        this.countryCode = country_code;
        this.prefix = prefix;
        if (number == null || number.isEmpty()) {
            throw new IllegalArgumentException("Phone number can't be empty");
        }
        this.number = number;
    }

    //TODO: Add tests
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] { countryCode, prefix, number });
    }

    @Override
    public boolean equals(Object anObject) {
        if (anObject instanceof PhoneNumber) {
            PhoneNumber phoneNumber = (PhoneNumber)anObject;
            return phoneNumber.getCountryCode().equalsIgnoreCase(this.countryCode) &&
                   phoneNumber.getPrefix().equalsIgnoreCase(this.prefix) &&
                   phoneNumber.getNumber().equalsIgnoreCase(this.number);
        }
        else
            return false;
    }

    @Override
    public String toString() {
        return "PhoneNumber{" +
                "countryCode='" + countryCode + '\'' +
                ", prefix='" + prefix + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
