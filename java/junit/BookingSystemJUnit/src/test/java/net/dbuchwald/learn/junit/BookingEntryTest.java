package net.dbuchwald.learn.junit;

import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by dawidbuchwald on 16.02.2017.
 */
public class BookingEntryTest {

    private static final LocalTime VALID_TIME_12_00 = LocalTime.of(12, 0);
    private static final LocalTime VALID_TIME_13_00 = LocalTime.of(13, 0);
    private static final LocalTime VALID_TIME_14_00 = LocalTime.of(14, 0);
    private static final LocalTime VALID_TIME_15_00 = LocalTime.of(15, 0);
    private static final LocalTime INVALID_TIME_13_15 = LocalTime.of(13, 15);

    @Test
    public void constructorShouldSetStartAndEndTimeCorrectly() {
        BookingEntry bookingEntry = new BookingEntry(VALID_TIME_12_00, VALID_TIME_15_00);
        assertEquals(VALID_TIME_12_00, bookingEntry.getStartTime());
        assertEquals(VALID_TIME_15_00, bookingEntry.getEndTime());
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldRejectEqualStartAndEndTime() {
        BookingEntry bookingEntry = new BookingEntry(VALID_TIME_12_00, VALID_TIME_12_00);
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldRejectEndTimeBeforeStartTime() {
        BookingEntry bookingEntry = new BookingEntry(VALID_TIME_13_00, VALID_TIME_12_00);
    }

    @SuppressWarnings({"unused", "ConstantConditions"})
    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldRejectNullStartTime() {
        BookingEntry bookingEntry = new BookingEntry(null, VALID_TIME_13_00);
    }

    @SuppressWarnings({"unused", "ConstantConditions"})
    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldRejectNullEndTime() {
        BookingEntry bookingEntry = new BookingEntry(VALID_TIME_12_00, null);
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldRejectNonFullHourStartTime() {
        BookingEntry bookingEntry = new BookingEntry(INVALID_TIME_13_15, VALID_TIME_14_00);
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldRejectNonFullHourEndTime() {
        BookingEntry bookingEntry = new BookingEntry(VALID_TIME_13_00, INVALID_TIME_13_15);
    }

    @Test
    public void equalsMethodShouldReturnTrueForEqualObjects() {
        BookingEntry bookingEntry1 = new BookingEntry(VALID_TIME_12_00, VALID_TIME_13_00);
        BookingEntry bookingEntry2 = new BookingEntry(VALID_TIME_12_00, VALID_TIME_13_00);
        assertTrue(bookingEntry1.equals(bookingEntry2));
    }

    @Test
    public void equalsMethodShouldReturnFalseForDifferentObjects() {
        BookingEntry bookingEntry1 = new BookingEntry(VALID_TIME_12_00, VALID_TIME_13_00);
        BookingEntry bookingEntry2 = new BookingEntry(VALID_TIME_13_00, VALID_TIME_14_00);
        assertFalse(bookingEntry1.equals(bookingEntry2));
    }

    @Test
    public void overlapsMethodShouldReturnFalseForNonOverlappingBookings() {
        BookingEntry bookingEntry1 = new BookingEntry(VALID_TIME_12_00, VALID_TIME_13_00);
        BookingEntry bookingEntry2 = new BookingEntry(VALID_TIME_14_00, VALID_TIME_15_00);
        assertFalse(bookingEntry1.overlaps(bookingEntry2));
    }

    @Test
    public void overlapsMethodShouldReturnFalseForAdjacentBookings() {
        BookingEntry bookingEntry1 = new BookingEntry(VALID_TIME_12_00, VALID_TIME_13_00);
        BookingEntry bookingEntry2 = new BookingEntry(VALID_TIME_13_00, VALID_TIME_14_00);
        assertFalse(bookingEntry1.overlaps(bookingEntry2));
    }

    @Test
    public void overlapsMethodShouldReturnTrueForOverlappingBookings() {
        BookingEntry bookingEntry1 = new BookingEntry(VALID_TIME_12_00, VALID_TIME_14_00);
        BookingEntry bookingEntry2 = new BookingEntry(VALID_TIME_13_00, VALID_TIME_15_00);
        assertTrue(bookingEntry1.overlaps(bookingEntry2));
    }

    @Test
    public void overlapsMethodShouldReturnTrueForNestedBookings() {
        BookingEntry bookingEntry1 = new BookingEntry(VALID_TIME_12_00, VALID_TIME_15_00);
        BookingEntry bookingEntry2 = new BookingEntry(VALID_TIME_13_00, VALID_TIME_14_00);
        assertTrue(bookingEntry1.overlaps(bookingEntry2));
    }
}
