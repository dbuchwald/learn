package net.dbuchwald.learn.junit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by dawidbuchwald on 14.02.2017.
 */
public class PhoneNumberTest {

    @Test
    public void constructorShouldSetCountryCodePrefixAndNumber() {
        PhoneNumber phoneNumber = new PhoneNumber("+48", "22", "5265700");

        assertEquals("Country code is not set correctly", "+48", phoneNumber.getCountryCode());
        assertEquals("Prefix is not set correctly", "22", phoneNumber.getPrefix());
        assertEquals("Number is not set correctly", "5265700", phoneNumber.getNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("unused")
    public void constructorShouldNotAcceptNullNumber() {
        PhoneNumber phoneNumber = new PhoneNumber("+48", "22", null);
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("unused")
    public void constructorShouldNotAcceptEmptyNumber() {
        PhoneNumber phoneNumber = new PhoneNumber("+48", "22", "");
    }

    @Test
    public void constructorShouldAcceptNullCountryCode() {
        PhoneNumber phoneNumber = new PhoneNumber(null, "22", "5265700");
        assertNotNull("Phone number not created correctly", phoneNumber);
    }

    @Test
    public void constructorShouldAcceptEmptyCountryCode() {
        PhoneNumber phoneNumber = new PhoneNumber("", "22", "5265700");
        assertNotNull("Phone number not created correctly", phoneNumber);
    }

    @Test
    public void differentPhoneNumbersAreNotEqual() {
        PhoneNumber phoneNumber1 = new PhoneNumber("+48", "22", "5265700");
        PhoneNumber phoneNumber2 = new PhoneNumber("+1", "800", "CALLSAUL");

        assertNotEquals("Phone numbers are equal", phoneNumber1, phoneNumber2);
    }

    @Test
    public void identicalPhoneNumbersAreEqual() {
        PhoneNumber phoneNumber1 = new PhoneNumber("+48", "22", "5265700");
        PhoneNumber phoneNumber2 = new PhoneNumber("+48", "22", "5265700");

        assertEquals("Phone numbers are not equal", phoneNumber1, phoneNumber2);
    }
}
