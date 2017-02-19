package net.dbuchwald.learn.junit;

/**
 * Created by dawidbuchwald on 17.02.2017.
 */
public interface Client {
    void receive(Message message);
    void receive(String category, Message message);
}
