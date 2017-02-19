package net.dbuchwald.learn.junit;

import java.awt.print.Book;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by dawidbuchwald on 18.02.2017.
 */
public class BookingSystemRevisited {

    private class ReservationEntry {
        private final DayOfWeek dayOfWeek;
        private final int hour;

        ReservationEntry(DayOfWeek dayOfWeek, int hour) {
            if (hour < 0 || hour > 23)
                throw new IllegalArgumentException("Invalid hour");
            this.dayOfWeek = dayOfWeek;
            this.hour = hour;
        }

        public DayOfWeek getDayOfWeek() {
            return dayOfWeek;
        }

        public int getHour() {
            return hour;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ReservationEntry) {
                ReservationEntry entry = (ReservationEntry)obj;
                return entry.getDayOfWeek().equals(this.dayOfWeek) &&
                        entry.getHour() == this.getHour();
            }
            else {
                return false;
            }
        }
    }

    private final Set<BookableResource> resources;
    private final Map<String, Set<ReservationEntry>> reservations;

    public BookingSystemRevisited(Set<BookableResource> resources) {
        if (resources == null)
            throw new IllegalArgumentException("Resources must not be null");

        if (resources.isEmpty())
            throw new IllegalArgumentException("Resources must not be empty");

        this.resources = new HashSet<>(resources);
        this.reservations = new HashMap<>();
        for (BookableResource resource: resources) {
            this.reservations.put(resource.getId(), new HashSet<>());
        }
    }

    public Set<BookableResource> listAllResources() {
        return new HashSet<>(resources);
    }

    public Set<BookableResource> listResources(int capacity, Set<Equipment> equipment) {
        Set<BookableResource> result = new HashSet<>();

        for (BookableResource resource: resources) {
            if (resource.getCapacity() >= capacity &&
                    (equipment == null || resource.getEquipment().containsAll(equipment)))
                result.add(resource);
        }

        return result;
    }

    public void bookResource(String resource, DayOfWeek dayOfWeek, int hour) {
        BookableResource bookableResource = null;
        for (BookableResource temporaryResource: resources) {
            if (temporaryResource.getId().equals(resource))
                bookableResource = temporaryResource;
        }

        if (bookableResource == null)
            throw new IllegalArgumentException("Resource with given Id does not exist");

        if (hour < 0 || hour > 23)
            throw new IllegalArgumentException("Incorrect hour provided");

        ReservationEntry reservationEntry = new ReservationEntry(dayOfWeek, hour);

        reservations.get(bookableResource.getId()).add(reservationEntry);
    }

    public Set<BookableResource> listAvailableResources(DayOfWeek dayOfWeek, int time) {
        Set<BookableResource> result = new HashSet<>();

        ReservationEntry reservationEntry = new ReservationEntry(dayOfWeek, time);

        for (BookableResource resource: resources) {
            if (!reservations.get(resource.getId()).contains(reservationEntry)) {
                result.add(resource);
            }
        }
        return result;
    }
}
