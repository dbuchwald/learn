package net.dbuchwald.learn.junit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Created by dawidbuchwald on 17.02.2017.
 */
public class RaceResultsEnhancedServiceTest {

    private final Set<String> categories = new HashSet<>();
    private static final int NUMBER_OF_CATEGORIES = 3;
    private static final String CATEGORY_F1 = "F1";
    private static final String CATEGORY_WRC = "WRC";
    private static final String CATEGORY_NASCAR = "NASCAR";
    private static final String CATEGORY_DOG_RACES = "Dog Race";
    private RaceResultsEnhancedService raceResults;

    private final LoggingService loggingService = mock(LoggingService.class);
    private final Message message = mock(Message.class);
    private final Message messageF1 = mock(Message.class, "F1 Message");
    private final Message messageWRC = mock(Message.class, "WRC Message");
    private final Message messageNASCAR = mock(Message.class, "NASCAR Message");
    private final Client clientA = mock(Client.class, "clientA");
    private final Client clientB = mock(Client.class, "clientB");

    @BeforeEach
    public void setUp() {
        categories.add(CATEGORY_F1);
        categories.add(CATEGORY_WRC);
        categories.add(CATEGORY_NASCAR);

        raceResults = new RaceResultsEnhancedService(loggingService, categories);

        when(message.getText()).thenReturn("Message");
        when(messageF1.getText()).thenReturn("F1 Message");
        when(messageWRC.getText()).thenReturn("WRC Message");
        when(messageNASCAR.getText()).thenReturn("NASCAR Message");
    }

    @Test
    public void constructorShouldRejectNullLoggingService() {
        assertThrows(IllegalArgumentException.class, () -> new RaceResultsEnhancedService(null, categories));
    }

    @Test
    public void constructorShouldSetUpCategoriesCorrectly() {
        Set<String> categories = raceResults.getCategories();

        assertEquals(NUMBER_OF_CATEGORIES, categories.size());
        assertTrue(categories.contains(CATEGORY_F1));
        assertTrue(categories.contains(CATEGORY_WRC));
        assertTrue(categories.contains(CATEGORY_NASCAR));
    }

    @Test
    public void categoriesShouldBeImmutable() {
        Set<String> categories = raceResults.getCategories();

        categories.add(CATEGORY_DOG_RACES);

        assertEquals(NUMBER_OF_CATEGORIES, raceResults.getCategories().size());
    }

    @Test
    public void constructorShouldRejectNullCategoriesList() {
        assertThrows(IllegalArgumentException.class, () -> new RaceResultsEnhancedService(loggingService, null));
    }

    @Test
    public void constructorShouldRejectEmptyCategoriesList() {
        assertThrows(IllegalArgumentException.class, () -> new RaceResultsEnhancedService(loggingService, new HashSet<>()));
    }

    @Test
    public void subscribeToInvalidCategoryShouldFail() {
        assertThrows(IllegalArgumentException.class, () -> raceResults.addSubscriber(clientA, CATEGORY_DOG_RACES));
    }

    @Test
    public void unsubscribeFromNotSubscribedCategoryShouldFail() {
        raceResults.addSubscriber(clientA, CATEGORY_F1);
        assertThrows(IllegalArgumentException.class, () -> raceResults.removeSubscriber(clientA, CATEGORY_WRC));
    }

    @Test
    public void notSubscribedClientShouldNotReceiveMessage() {
        raceResults.send(CATEGORY_F1, message);
        verify(clientA, never()).receive(CATEGORY_F1, message);
        verify(clientB, never()).receive(CATEGORY_F1, message);
    }

    @Test
    public void clientSubscribedToDifferentCategoryShouldNotReceiveMessage() {
        raceResults.addSubscriber(clientA, CATEGORY_F1);
        raceResults.addSubscriber(clientB, CATEGORY_F1);
        raceResults.addSubscriber(clientB, CATEGORY_NASCAR);
        raceResults.send(CATEGORY_WRC, message);
        verify(clientA, never()).receive(CATEGORY_WRC, message);
        verify(clientB, never()).receive(CATEGORY_WRC, message);
    }

    @Test
    public void subscribedClientShouldReceiveMessage() {
        raceResults.addSubscriber(clientA, CATEGORY_F1);
        raceResults.send(CATEGORY_F1, message);
        verify(clientA).receive(CATEGORY_F1, message);
    }

    @Test
    public void clientSubscribedToTwoCategoriesShouldReceiveMessagesFromBoth() {
        raceResults.addSubscriber(clientA, CATEGORY_F1);
        raceResults.addSubscriber(clientA, CATEGORY_NASCAR);
        raceResults.send(CATEGORY_F1, message);
        raceResults.send(CATEGORY_WRC, message);
        raceResults.send(CATEGORY_NASCAR, message);
        verify(clientA).receive(CATEGORY_F1, message);
        verify(clientA, never()).receive(CATEGORY_WRC, message);
        verify(clientA).receive(CATEGORY_NASCAR, message);
    }

    @Test
    public void messagesShouldBeSentInProperCategories() {
        raceResults.addSubscriber(clientA, CATEGORY_F1);
        raceResults.addSubscriber(clientA, CATEGORY_WRC);
        raceResults.addSubscriber(clientA, CATEGORY_NASCAR);
        raceResults.send(CATEGORY_F1, messageF1);
        raceResults.send(CATEGORY_WRC, messageWRC);
        raceResults.send(CATEGORY_NASCAR, messageNASCAR);
        verify(clientA).receive(CATEGORY_F1, messageF1);
        verify(clientA).receive(CATEGORY_WRC, messageWRC);
        verify(clientA).receive(CATEGORY_NASCAR, messageNASCAR);
    }

    @Test
    public void allSubscribedClientsShouldReceiveMessages() {
        raceResults.addSubscriber(clientA, CATEGORY_F1);
        raceResults.addSubscriber(clientB, CATEGORY_F1);
        raceResults.send(CATEGORY_F1, message);
        verify(clientA).receive(CATEGORY_F1, message);
        verify(clientB).receive(CATEGORY_F1, message);
    }

    @Test
    public void shouldSendOnlyOneMessageToMultiSubscriber() {
        raceResults.addSubscriber(clientA, CATEGORY_F1);
        raceResults.addSubscriber(clientA, CATEGORY_F1);
        raceResults.send(CATEGORY_F1, message);
        verify(clientA, times(1)).receive(CATEGORY_F1, message);
    }

    @Test
    public void unsubscribedClientShouldNotReceiveMessages() {
        raceResults.addSubscriber(clientA, CATEGORY_F1);
        raceResults.removeSubscriber(clientA, CATEGORY_F1);
        raceResults.send(CATEGORY_F1, message);
        verify(clientA, never()).receive(CATEGORY_F1, message);
    }

    @Test
    public void noMessageShouldBeLoggedWhenNoMessageIsSent() {
        raceResults.addSubscriber(clientA, CATEGORY_F1);
        raceResults.removeSubscriber(clientA, CATEGORY_F1);
        verify(loggingService, never()).log(message.getText());
    }

    @Test
    public void messageShouldBeLoggedWhenSent() {
        raceResults.send(CATEGORY_F1, message);
        verify(loggingService).log(message.getText());
    }

    @Test
    public void multipleMessagesShouldBeLogged() {
        raceResults.send(CATEGORY_F1, messageF1);
        raceResults.send(CATEGORY_WRC, messageWRC);

        verify(loggingService).log(messageF1.getText());
        verify(loggingService).log(messageWRC.getText());
    }
}
