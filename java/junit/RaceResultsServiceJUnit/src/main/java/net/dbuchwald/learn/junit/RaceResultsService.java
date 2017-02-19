package net.dbuchwald.learn.junit;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by dawidbuchwald on 17.02.2017.
 */
public class RaceResultsService {

    private final Collection<Client> clients = new HashSet<>();

    public void addSubscriber(Client client) {
        clients.add(client);
    }

    public void send(Message message) {
        for (Client client: clients) {
            client.receive(message);
        }
    }

    public void removeSubscriber(Client client) {
        clients.remove(client);
    }
}
