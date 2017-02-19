package net.dbuchwald.learn.junit;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dawidbuchwald on 16.02.2017.
 */
public class BookingService {

    private String bookingResourceName;
    private final List<BookingEntry> bookings = new LinkedList<>();

    public BookingService(String bookingResourceName) {
        if (bookingResourceName == null || bookingResourceName.isEmpty())
            throw new IllegalArgumentException("Resource name must be provided");
        this.bookingResourceName = bookingResourceName;
    }

    public String getBookingResourceName() {
        return bookingResourceName;
    }

    public void book(LocalTime startTime, LocalTime endTime) {
        BookingEntry newEntry = new BookingEntry(startTime, endTime);
        for (BookingEntry existingBooking: bookings) {
            if (existingBooking.overlaps(newEntry))
                throw new IllegalArgumentException("Bookings must not overlap");
        }
        bookings.add(newEntry);
    }

    public List<BookingEntry> getBookings() {
        return bookings;
    }
}
