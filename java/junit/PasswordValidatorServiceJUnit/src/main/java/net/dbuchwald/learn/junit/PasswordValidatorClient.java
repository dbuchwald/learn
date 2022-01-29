package net.dbuchwald.learn.junit;

/**
 * Created by dawidbuchwald on 15.02.2017.
 */
public class PasswordValidatorClient {

    public static void main(String[] args) {
        PasswordValidatorService service = new PasswordValidatorService(8, 1, "#".toCharArray());

        System.out.println(service.validatePassword("Kupaki1#"));
    }
}
