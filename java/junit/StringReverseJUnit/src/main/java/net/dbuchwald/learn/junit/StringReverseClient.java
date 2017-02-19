package net.dbuchwald.learn.junit;

/**
 * Created by dawidbuchwald on 15.02.2017.
 */
public class StringReverseClient {

    public static void main(String args[]) {
        String sample = "Test";

        System.out.println("Reverse of " + sample + " is <" + StringReverseService.reverse(sample) + ">");
    }
}
