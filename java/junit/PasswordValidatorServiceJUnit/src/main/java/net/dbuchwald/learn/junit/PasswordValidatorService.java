package net.dbuchwald.learn.junit;

/**
 * Created by dawidbuchwald on 15.02.2017.
 */
public class PasswordValidatorService {

    private final int minimumPasswordLength;
    private final int minimumNumberOfDigits;
    private final char[] requiredCharacters;

    public PasswordValidatorService(int minimumPasswordLength, int minimumNumberOfDigits, char[] requiredCharacters) {
        if (minimumPasswordLength <= 0) throw new IllegalArgumentException("Password length must be positive");
        if (minimumNumberOfDigits < 0) throw new IllegalArgumentException("Negative values not allowed");
        this.minimumPasswordLength = minimumPasswordLength;
        this.minimumNumberOfDigits = minimumNumberOfDigits;
        this.requiredCharacters = requiredCharacters;
    }

    public int getMinimumPasswordLength() {
        return minimumPasswordLength;
    }

    public int getMinimumNumberOfDigits() {
        return minimumNumberOfDigits;
    }

    public char[] getRequiredCharacters() {
        return requiredCharacters;
    }

    public boolean validatePassword(String password) {
        if (password == null) return false;
        else if (password.isEmpty()) return false;
        else if (password.length()<minimumPasswordLength) return false;
        else {
            int numberOfDigits = 0;
            int numberOfUpperCaseCharacters = 0;
            int numberOfLowerCaseCharacters = 0;
            for (char c: password.toCharArray()) {
                if (Character.isDigit(c)) numberOfDigits++;
                if (Character.isUpperCase(c)) numberOfUpperCaseCharacters++;
                if (Character.isLowerCase(c)) numberOfLowerCaseCharacters++;
            }
            if (numberOfDigits<minimumNumberOfDigits) return false;
            if (numberOfUpperCaseCharacters == 0) return false;
            if (numberOfLowerCaseCharacters == 0) return false;
            for (char required: requiredCharacters) {
                boolean exists = false;
                for (char c: password.toCharArray()) {
                    if (c == required) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) return false;
            }
        }
        return true;
    }
}
