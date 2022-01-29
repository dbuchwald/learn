package net.dbuchwald.learn.junit;

/**
 * Created by dawidbuchwald on 15.02.2017.
 */
public class StringReverseService {

    public static String reverse(String s) {
        if (s == null)
            return null;
        else if (s.length() <= 1)
            return s;
        else
            return s.substring(s.length()-1) + reverse(s.substring(0, s.length()-1));
    }
}
