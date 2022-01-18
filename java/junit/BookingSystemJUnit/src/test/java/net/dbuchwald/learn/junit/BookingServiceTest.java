package net.dbuchwald.learn.junit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by dawidbuchwald on 16.02.2017.
 */
public class BookingServiceTest {

    private static final String BOOKING_RESOURCE_NAME = "Classroom";
    private static final String EMPTY_BOOKING_RESOURCE_NAME = "";
    private static final LocalTime INVALID_TIME_12_15 = LocalTime.of(12, 15, 0);
    private static final LocalTime VALID_TIME_14_00 = LocalTime.of(14, 0, 0);
    private static final LocalTime VALID_TIME_12_00 = LocalTime.of(12, 0, 0);
    private static final LocalTime INVALID_TIME_14_00_05 = LocalTime.of(14, 0, 5);
    private static final LocalTime VALID_TIME_13_00 = LocalTime.of(13, 0);
    private static final LocalTime VALID_TIME_15_00 = LocalTime.of(15, 0);


    private BookingService bookingService;

    @BeforeEach
    public void setUp() {
        bookingService = new BookingService(BOOKING_RESOURCE_NAME);
    }

    @Test
    public void constructorShouldSetBookingResourceName() {
        assertEquals(BOOKING_RESOURCE_NAME, bookingService.getBookingResourceName());
    }

    @Test
    public void constructorShouldRejectNullResourceName() {
        assertThrows(IllegalArgumentException.class, () -> new BookingService(null));
    }

    @Test
    public void constructorShouldRejectEmptyResourceName() {
        assertThrows(IllegalArgumentException.class, () -> new BookingService(EMPTY_BOOKING_RESOURCE_NAME));
    }

    @Test
    public void bookingShouldBeRejectedForNullStartTime() {
        assertThrows(IllegalArgumentException.class, () -> bookingService.book(null, VALID_TIME_13_00));
    }

    @Test
    public void bookingShouldBeRejectedForNullEndTime() {
        assertThrows(IllegalArgumentException.class, () -> bookingService.book(VALID_TIME_12_00, null));
    }

    @Test
    public void bookingShouldBeRejectedForNonFullHoursStart() {
        assertThrows(IllegalArgumentException.class, () -> bookingService.book(INVALID_TIME_12_15, VALID_TIME_14_00));
    }

    @Test
    public void bookingShouldBeRejectedForNonFullHoursEnd() {
        assertThrows(IllegalArgumentException.class, () -> bookingService.book(VALID_TIME_12_00, INVALID_TIME_14_00_05));
    }

    @Test
    public void startTimeMustBeBeforeEndTime() {
        assertThrows(IllegalArgumentException.class, () -> bookingService.book(VALID_TIME_14_00, VALID_TIME_12_00));
    }

    @Test
    public void startTimeMustBeDifferentThanEndTime() {
        assertThrows(IllegalArgumentException.class, () -> bookingService.book(VALID_TIME_14_00, VALID_TIME_14_00));
    }

    @Test
    public void bookingPeriodsMustNotOverlap() {
        bookingService.book(VALID_TIME_12_00, VALID_TIME_14_00);
        assertThrows(IllegalArgumentException.class, () -> bookingService.book(VALID_TIME_13_00, VALID_TIME_15_00));
    }

    @Test
    public void bookingPeriodsMayBeAdjacent() {
        bookingService.book(VALID_TIME_12_00, VALID_TIME_13_00);
        bookingService.book(VALID_TIME_13_00, VALID_TIME_15_00);
    }

    @Test
    public void emptyBookingsListShouldBeReturnedBeforeFirstReservation() {
        assertTrue(bookingService.getBookings().isEmpty());
    }

    @Test
    public void singleBookingShouldBeCorrectlyReturned() {
        bookingService.book(VALID_TIME_12_00, VALID_TIME_13_00);
        assertEquals(1, bookingService.getBookings().size());
        BookingEntry entry = bookingService.getBookings().get(0);
        assertEquals(VALID_TIME_12_00, entry.getStartTime());
        assertEquals(VALID_TIME_13_00, entry.getEndTime());
    }

    @Test
    public void doubleBookingsShouldBeCorrectlyReturned() {
        bookingService.book(VALID_TIME_12_00, VALID_TIME_13_00);
        bookingService.book(VALID_TIME_14_00, VALID_TIME_15_00);
        List<BookingEntry> bookings = bookingService.getBookings();
        assertEquals(2, bookings.size());
        assertTrue(bookings.contains(new BookingEntry(VALID_TIME_12_00, VALID_TIME_13_00)));
        assertTrue(bookings.contains(new BookingEntry(VALID_TIME_14_00, VALID_TIME_15_00)));
    }
}
