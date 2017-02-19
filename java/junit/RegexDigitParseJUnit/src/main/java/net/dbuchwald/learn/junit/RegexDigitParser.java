package net.dbuchwald.learn.junit;

/**
 * Created by dawidbuchwald on 16.02.2017.
 */
public class RegexDigitParser {
    private final int numberOfConsecutiveDigits;

    public RegexDigitParser(int numberOfConsecutiveDigits) {
        this.numberOfConsecutiveDigits = numberOfConsecutiveDigits;
    }

    public int getNumberOfConsecutiveDigits() {
        return numberOfConsecutiveDigits;
    }

    //TODO: Rewrite for actual RegEx based implementation
    public String parse(String input) {

        StringBuilder result = new StringBuilder();
        StringBuilder currentDigits = new StringBuilder();

        for (char c: input.toCharArray()) {
            if (Character.isDigit(c)) {
                currentDigits.append(c);
            } else {
                if (currentDigits.toString().length() >= numberOfConsecutiveDigits) {
                    if (result.length() >0 ) {
                        result.append(", ");
                    }
                    result.append(currentDigits.toString());
                }
                if (currentDigits.length() > 0) currentDigits = new StringBuilder();
            }
        }

        return result.toString();
    }
}
