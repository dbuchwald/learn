package net.dbuchwald.learn.junit;

/**
 * Created by dawidbuchwald on 17.02.2017.
 */
public interface User {
    String getPassword();

    void setPassword(String passwordMd5);
}
