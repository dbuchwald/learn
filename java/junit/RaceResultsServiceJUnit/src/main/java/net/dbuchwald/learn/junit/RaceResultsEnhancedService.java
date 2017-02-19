package net.dbuchwald.learn.junit;

import java.util.*;

/**
 * Created by dawidbuchwald on 17.02.2017.
 */
public class RaceResultsEnhancedService {

    private final Set<String> categories;
    private final Map<String, Set<Client>> subscriptions = new HashMap<>();
    private final LoggingService loggingService;

    public RaceResultsEnhancedService(LoggingService loggingService, Set<String> categories) {
        if (loggingService == null)
            throw new IllegalArgumentException("LoggingService must not be null");
        if (categories == null)
            throw new IllegalArgumentException("Categories can't be null");
        if (categories.isEmpty())
            throw new IllegalArgumentException("Categories can't be empty");
        this.categories = categories;
        this.loggingService = loggingService;
        for (String category: categories)
            subscriptions.put(category, new HashSet<>());
    }

    public void addSubscriber(Client client, String category) {
        if (!categories.contains(category))
            throw new IllegalArgumentException("Invalid category");
        subscriptions.get(category).add(client);
    }

    public void send(String category, Message message) {
        loggingService.log(message.getText());
        for (Client client: subscriptions.get(category)) {
            client.receive(category, message);
        }
    }

    public void removeSubscriber(Client client, String category) {
        if (!subscriptions.get(category).contains(client))
            throw new IllegalArgumentException("Client not subscribed to selected category");
        subscriptions.get(category).remove(client);
    }

    public Set<String> getCategories() {
        return new HashSet<>(categories);
    }
}
