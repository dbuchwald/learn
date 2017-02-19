package net.dbuchwald.learn.junit;

import java.time.LocalTime;

/**
 * Created by dawidbuchwald on 16.02.2017.
 */
public class BookingEntry {

    private final LocalTime startTime;
    private final LocalTime endTime;

    public BookingEntry(LocalTime startTime, LocalTime endTime) {

        if (startTime == null)
            throw new IllegalArgumentException("Start time must not be null");

        if (endTime == null)
            throw new IllegalArgumentException("End time must not be null");

        if (startTime.getMinute() != 0 || startTime.getSecond() != 0)
            throw new IllegalArgumentException("Only full hours can be used as start time");

        if (endTime.getMinute() != 0 || endTime.getSecond() != 0)
            throw new IllegalArgumentException("Only full hours can be used as end time");

        if (startTime.equals(endTime))
            throw new IllegalArgumentException("Start time must be different than end time");

        if (endTime.isBefore(startTime))
            throw new IllegalArgumentException("Start time must be before end time");

        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BookingEntry) {
            BookingEntry anotherBookingEntry = (BookingEntry)obj;
            return anotherBookingEntry.getStartTime().equals(startTime) &&
                   anotherBookingEntry.getEndTime().equals(endTime);
        } else return false;
    }

    public boolean overlaps(BookingEntry anotherEntry) {
        return anotherEntry.getEndTime().isAfter(startTime) &&
                anotherEntry.getStartTime().isBefore(endTime);
    }
}
