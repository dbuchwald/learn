package net.dbuchwald.learn.junit;

import java.time.DayOfWeek;
import java.util.*;

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

    public Set<BookableResource> listResources(int capacity) {
        Set<BookableResource> result = new HashSet<>();

        for (BookableResource resource: resources) {
            if (resource.getCapacity() >= capacity)
                result.add(resource);
        }

        return result;
    }

    public Set<BookableResource> listResources(int capacity, Equipment equipment) {
        Set<BookableResource> result = new HashSet<>();

        for (BookableResource resource: resources) {
            if (resource.getCapacity() >= capacity &&
                    resource.getEquipment().contains(equipment))
                result.add(resource);
        }

        return result;
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

    public void bookResource(String resourceId, DayOfWeek dayOfWeek, int hour) {
        BookableResource bookableResource = null;
        for (BookableResource temporaryResource: resources) {
            if (temporaryResource.getId().equals(resourceId))
                bookableResource = temporaryResource;
        }

        if (bookableResource == null)
            throw new IllegalArgumentException("Resource with given Id does not exist");

        if (hour < 0 || hour > 23)
            throw new IllegalArgumentException("Incorrect hour provided");

        ReservationEntry reservationEntry = new ReservationEntry(dayOfWeek, hour);

        Set<ReservationEntry> currentReservations = reservations.get(bookableResource.getId());
        for (ReservationEntry currentReservation: currentReservations) {
            if (currentReservation.equals(reservationEntry)) {
                throw new IllegalArgumentException("Resource is already booked for this time slot");
            }
        }

        reservations.get(bookableResource.getId()).add(reservationEntry);
    }

    public void bookResource(int capacity, Equipment equipment, DayOfWeek dayOfWeek, int hour) {
        Set<BookableResource> resources = listResources(capacity, equipment);

        if (resources.size() == 0)
            throw new IllegalArgumentException("No resources satisfy search criteria");

        if (resources.size() > 1)
            throw new IllegalArgumentException("Provided criteria are ambiguous");

        bookResource(resources.iterator().next().getId(), dayOfWeek, hour);
    }

    public void bookResource(int capacity, Set<Equipment> equipment, DayOfWeek dayOfWeek, int hour) {
        Set<BookableResource> resources = listResources(capacity, equipment);

        if (resources.size() == 0)
            throw new IllegalArgumentException("No resources satisfy search criteria");

        if (resources.size() > 1)
            throw new IllegalArgumentException("Provided criteria are ambiguous");

        bookResource(resources.iterator().next().getId(), dayOfWeek, hour);
    }

    public Set<BookableResource> listAvailableResources(DayOfWeek dayOfWeek, int hour) {
        Set<BookableResource> result = new HashSet<>();

        ReservationEntry reservationEntry = new ReservationEntry(dayOfWeek, hour);

        for (BookableResource resource: resources) {
            Set<ReservationEntry> currentReservations = reservations.get(resource.getId());
            boolean conflictingReservationFound = false;
            for (ReservationEntry currentReservation: currentReservations) {
                if (currentReservation.equals(reservationEntry)) {
                    conflictingReservationFound = true;
                }
            }
            if (!conflictingReservationFound) {
                result.add(resource);
            }
        }
        return result;
    }
}
