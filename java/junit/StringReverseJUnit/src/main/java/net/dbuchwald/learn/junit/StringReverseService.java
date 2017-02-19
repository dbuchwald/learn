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
//        List<String> tempArray = new ArrayList<String>(s.length());
//        for (int i = 0; i < s.length(); i++) {
//            tempArray.add(s.substring(i, i+1));
//        }
//        StringBuilder reversedString = new StringBuilder(s.length());
//        for (int i = tempArray.size() -1; i >= 0; i--) {
//            reversedString.append(tempArray.get(i));
//        }
//        return reversedString.toString();
    }
}
