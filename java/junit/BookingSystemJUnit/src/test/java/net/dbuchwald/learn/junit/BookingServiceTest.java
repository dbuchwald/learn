package net.dbuchwald.learn.junit;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @Before
    public void setUp() {
        bookingService = new BookingService(BOOKING_RESOURCE_NAME);
    }

    @Test
    public void constructorShouldSetBookingResourceName() {
        assertEquals(BOOKING_RESOURCE_NAME, bookingService.getBookingResourceName());
    }

    @SuppressWarnings({"unused", "ConstantConditions"})
    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldRejectNullResourceName() {
        BookingService bookingService = new BookingService(null);
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldRejectEmptyResourceName() {
        BookingService bookingService = new BookingService(EMPTY_BOOKING_RESOURCE_NAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bookingShouldBeRejectedForNullStartTime() {
        bookingService.book(null, VALID_TIME_13_00);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bookingShouldBeRejectedForNullEndTime() {
        bookingService.book(VALID_TIME_12_00, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bookingShouldBeRejectedForNonFullHoursStart() {
        bookingService.book(INVALID_TIME_12_15, VALID_TIME_14_00);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bookingShouldBeRejectedForNonFullHoursEnd() {
        bookingService.book(VALID_TIME_12_00, INVALID_TIME_14_00_05);
    }

    @Test(expected = IllegalArgumentException.class)
    public void startTimeMustBeBeforeEndTime() {
        bookingService.book(VALID_TIME_14_00, VALID_TIME_12_00);
    }

    @Test(expected = IllegalArgumentException.class)
    public void startTimeMustBeDifferentThanEndTime() {
        bookingService.book(VALID_TIME_14_00, VALID_TIME_14_00);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bookingPeriodsMustNotOverlap() {
        bookingService.book(VALID_TIME_12_00, VALID_TIME_14_00);
        bookingService.book(VALID_TIME_13_00, VALID_TIME_15_00);
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
