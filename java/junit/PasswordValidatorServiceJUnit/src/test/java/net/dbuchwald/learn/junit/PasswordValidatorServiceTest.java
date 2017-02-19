package net.dbuchwald.learn.junit;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by dawidbuchwald on 15.02.2017.
 */
public class PasswordValidatorServiceTest {

    private static final int INVALID_MINIMUM_PASSWORD_LENGTH = 0;
    private static final int INVALID_MINIMUM_NUMBER_OF_DIGITS = -10;
    private static final int MINIMUM_PASSWORD_LENGTH = 12;
    private static final int MINIMUM_NUMBER_OF_DIGITS = 3;
    private static final char[] REQUIRED_CHARACTERS = {'#', '_'};
    private static final String INVALID_EMPTY_PASSWORD = "";
    private static final String INVALID_PASSWORD_TOO_SHORT = "123#_ABC";
    private static final String INVALID_PASSWORD_TOO_FEW_DIGITS = "Password#_12";
    private static final String INVALID_PASSWORD_ONLY_LOWERCASE = "password123#_";
    private static final String INVALID_PASSWORD_ONLY_UPPERCASE = "PASSWORD123#_";
    private static final String INVALID_PASSWORD_MISSING_REQUIRED_CHARACTERS = "Password123#";
    private static final String VALID_PASSWORD = "Password#123_";

    private PasswordValidatorService passwordValidatorService;

    @Before
    public void setUp() {
        passwordValidatorService = new PasswordValidatorService(MINIMUM_PASSWORD_LENGTH,
                                                                MINIMUM_NUMBER_OF_DIGITS,
                                                                REQUIRED_CHARACTERS);
    }

    @Test
    public void constructorShouldSetUpValidatorFields() {
        assertEquals("Incorrect minimum password length", MINIMUM_PASSWORD_LENGTH,
                passwordValidatorService.getMinimumPasswordLength());
        assertEquals("Incorrect minimum number of digits", MINIMUM_NUMBER_OF_DIGITS,
                passwordValidatorService.getMinimumNumberOfDigits());
        assertEquals("Incorrect set of required characters", REQUIRED_CHARACTERS,
                passwordValidatorService.getRequiredCharacters());
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldRejectNonPositivePasswordLength() {
        passwordValidatorService = new PasswordValidatorService(INVALID_MINIMUM_PASSWORD_LENGTH,
                                                                MINIMUM_NUMBER_OF_DIGITS,
                                                                REQUIRED_CHARACTERS);
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldRejectNegativeNumberOfDigits() {
        passwordValidatorService = new PasswordValidatorService(MINIMUM_PASSWORD_LENGTH,
                                                                INVALID_MINIMUM_NUMBER_OF_DIGITS,
                                                                REQUIRED_CHARACTERS);
    }

    @Test
    public void nullPasswordIsNotValid() {
        assertFalse(passwordValidatorService.validatePassword(null));
    }

    @Test
    public void emptyPasswordIsNotValid() {
        assertFalse(passwordValidatorService.validatePassword(INVALID_EMPTY_PASSWORD));
    }

    @Test
    public void tooShortPasswordIsNotValid() {
        assertFalse(passwordValidatorService.validatePassword(INVALID_PASSWORD_TOO_SHORT));
    }

    @Test
    public void passwordWithTooFewDigitsIsInvalid() {
        assertFalse(passwordValidatorService.validatePassword(INVALID_PASSWORD_TOO_FEW_DIGITS));
    }

    @Test
    public void passwordWithNoUpperCaseLettersIsInvalid() {
        assertFalse(passwordValidatorService.validatePassword(INVALID_PASSWORD_ONLY_LOWERCASE));
    }

    @Test
    public void passwordWithNoLowerCaseLettersIsInvalid() {
        assertFalse(passwordValidatorService.validatePassword(INVALID_PASSWORD_ONLY_UPPERCASE));
    }

    @Test
    public void passwordMustContainAllSpecialCharacters() {
        assertFalse(passwordValidatorService.validatePassword(INVALID_PASSWORD_MISSING_REQUIRED_CHARACTERS));
    }

    @Test
    public void passwordMeetingRequirementsIsValid() {
        assertTrue(passwordValidatorService.validatePassword(VALID_PASSWORD));
    }
}
