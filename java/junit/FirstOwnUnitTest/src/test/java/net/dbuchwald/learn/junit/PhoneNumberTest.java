package net.dbuchwald.learn.junit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by dawidbuchwald on 14.02.2017.
 */
public class PhoneNumberTest {

    @Test
    public void constructorShouldSetCountryCodePrefixAndNumber() {
        PhoneNumber phoneNumber = new PhoneNumber("+48", "22", "5265700");

        assertEquals("+48", phoneNumber.getCountryCode(), "Country code is not set correctly");
        assertEquals("22", phoneNumber.getPrefix(), "Prefix is not set correctly");
        assertEquals("5265700", phoneNumber.getNumber(), "Number is not set correctly");
    }

    @Test
    public void constructorShouldNotAcceptNullNumber() {
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("+48", "22", null));
    }

    @Test
    public void constructorShouldNotAcceptEmptyNumber() {
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("+48", "22", ""));
    }

    @Test
    public void constructorShouldAcceptNullCountryCode() {
        PhoneNumber phoneNumber = new PhoneNumber(null, "22", "5265700");
        assertNotNull(phoneNumber, "Phone number not created correctly");
    }

    @Test
    public void constructorShouldAcceptEmptyCountryCode() {
        PhoneNumber phoneNumber = new PhoneNumber("", "22", "5265700");
        assertNotNull(phoneNumber, "Phone number not created correctly");
    }

    @Test
    public void differentPhoneNumbersAreNotEqual() {
        PhoneNumber phoneNumber1 = new PhoneNumber("+48", "22", "5265700");
        PhoneNumber phoneNumber2 = new PhoneNumber("+1", "800", "CALLSAUL");

        assertNotEquals(phoneNumber1, phoneNumber2, "Phone numbers are equal");
    }

    @Test
    public void identicalPhoneNumbersAreEqual() {
        PhoneNumber phoneNumber1 = new PhoneNumber("+48", "22", "5265700");
        PhoneNumber phoneNumber2 = new PhoneNumber("+48", "22", "5265700");

        assertEquals(phoneNumber1, phoneNumber2, "Phone numbers are not equal");
    }
}
