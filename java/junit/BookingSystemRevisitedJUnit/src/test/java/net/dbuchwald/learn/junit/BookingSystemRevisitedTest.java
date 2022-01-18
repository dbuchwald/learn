package net.dbuchwald.learn.junit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Created by dawidbuchwald on 18.02.2017.
 */
public class BookingSystemRevisitedTest {

    private static final int NUMBER_OF_CLASSROOMS_FOR_12_OR_MORE = 2;
    private static final int NUMBER_OF_CLASSROOMS = 3;
    private static final int CAPACITY_8_PEOPLE = 8;
    private static final int CAPACITY_12_PEOPLE = 12;
    private static final int CAPACITY_50_PEOPLE = 50;
    private static final int CAPACITY_60_PEOPLE = 60;
    private static final int NUMBER_OF_CLASSROOMS_WITH_PHONE = 2;
    private static final int NUMBER_OF_CLASSROOMS_WITH_PHONE_AND_PROJECTOR = 1;
    private static final int TIME_12_OCLOCK = 12;
    private static final int INVALID_TIME_27_OCLOCK = 27;
    private BookingSystemRevisited bookingSystem;
    private final Set<BookableResource> resources = new HashSet<>();
    private static final String CLASSROOM_A_ID = "Classroom A";
    private static final String CLASSROOM_B_ID = "Classroom B";
    private static final String CLASSROOM_LARGE_ID = "Large Classroom";
    private static final String NON_EXISTING_CLASSROOM = "Classroom X";
    private final BookableResource classroomA = mock(BookableResource.class, CLASSROOM_A_ID);
    private final BookableResource classroomB = mock(BookableResource.class, CLASSROOM_B_ID);
    private final BookableResource classroomLarge = mock(BookableResource.class, CLASSROOM_LARGE_ID);
    private final Set<Equipment> classroomAEquipment = new HashSet<>();
    private final Set<Equipment> classroomBEquipment = new HashSet<>();
    private final Set<Equipment> classroomLargeEquipment = new HashSet<>();

    @BeforeEach
    public void setUp() {
        when(classroomA.getId()).thenReturn(CLASSROOM_A_ID);
        when(classroomB.getId()).thenReturn(CLASSROOM_B_ID);
        when(classroomLarge.getId()).thenReturn(CLASSROOM_LARGE_ID);

        when(classroomA.getCapacity()).thenReturn(CAPACITY_8_PEOPLE);
        when(classroomB.getCapacity()).thenReturn(CAPACITY_12_PEOPLE);
        when(classroomLarge.getCapacity()).thenReturn(CAPACITY_50_PEOPLE);

        classroomAEquipment.add(Equipment.PHONE);
        when(classroomA.getEquipment()).thenReturn(classroomAEquipment);

        classroomBEquipment.add(Equipment.PROJECTOR);
        classroomBEquipment.add(Equipment.WHITEBOARD);
        when(classroomB.getEquipment()).thenReturn(classroomBEquipment);

        classroomLargeEquipment.add(Equipment.WHITEBOARD);
        classroomLargeEquipment.add(Equipment.PROJECTOR);
        classroomLargeEquipment.add(Equipment.PHONE);
        when(classroomLarge.getEquipment()).thenReturn(classroomLargeEquipment);

        resources.add(classroomA);
        resources.add(classroomB);
        resources.add(classroomLarge);

        bookingSystem = new BookingSystemRevisited(resources);
    }

    @Test
    public void constructorShouldRejectNullResources() {
        assertThrows(IllegalArgumentException.class, () -> new BookingSystemRevisited(null));
    }

    @Test
    public void constructorShouldRejectEmptyResources() {
        assertThrows(IllegalArgumentException.class, () -> new BookingSystemRevisited(new HashSet<>()));
    }

    @Test
    public void constructorShouldPopulateResources() {
        Set<BookableResource> resources = bookingSystem.listAllResources();

        assertEquals(NUMBER_OF_CLASSROOMS, resources.size());
        assertTrue(resources.contains(classroomA));
        assertTrue(resources.contains(classroomB));
        assertTrue(resources.contains(classroomLarge));
    }

    @Test
    public void listResourcesShouldEnableCapacitySearch() {
        Set<BookableResource> resources = bookingSystem.listResources(CAPACITY_12_PEOPLE);

        assertEquals(NUMBER_OF_CLASSROOMS_FOR_12_OR_MORE, resources.size());

        assertTrue(resources.contains(classroomB));
        assertTrue(resources.contains(classroomLarge));
    }

    @Test
    public void listResourcesShouldEnableEquipmentSearch() {
        Set<BookableResource> resources = bookingSystem.listResources(0, Equipment.PHONE);

        assertEquals(NUMBER_OF_CLASSROOMS_WITH_PHONE, resources.size());

        assertTrue(resources.contains(classroomA));
        assertTrue(resources.contains(classroomLarge));
    }

    @Test
    public void listResourcesShouldEnableSetOfEquipmentSearch() {
        Set<Equipment> equipment = new HashSet<>();
        equipment.add(Equipment.PHONE);

        Set<BookableResource> resources = bookingSystem.listResources(0, equipment);

        assertEquals(NUMBER_OF_CLASSROOMS_WITH_PHONE, resources.size());

        assertTrue(resources.contains(classroomA));
        assertTrue(resources.contains(classroomLarge));
    }

    @Test
    public void listResourcesShouldRequireAllEquipmentItems() {
        Set<Equipment> equipment = new HashSet<>();
        equipment.add(Equipment.PHONE);
        equipment.add(Equipment.PROJECTOR);

        Set<BookableResource> resources = bookingSystem.listResources(0, equipment);

        assertEquals(NUMBER_OF_CLASSROOMS_WITH_PHONE_AND_PROJECTOR, resources.size());

        assertTrue(resources.contains(classroomLarge));
    }

    @Test
    public void bookingShouldRejectNonExistentResource() {
        assertThrows(IllegalArgumentException.class, () -> bookingSystem.bookResource(NON_EXISTING_CLASSROOM, DayOfWeek.MONDAY, TIME_12_OCLOCK));
    }

    @Test
    public void bookingShouldRejectInvalidTime() {
        assertThrows(IllegalArgumentException.class, () -> bookingSystem.bookResource(CLASSROOM_A_ID, DayOfWeek.THURSDAY, INVALID_TIME_27_OCLOCK));
    }

    @Test
    public void bookingShouldAcceptValidParameters() {
        bookingSystem.bookResource(CLASSROOM_B_ID, DayOfWeek.WEDNESDAY, TIME_12_OCLOCK);
    }

    @Test
    public void listAvailableResourcesShouldReturnValidValue() {
        Set<BookableResource> availableResources = bookingSystem.listAvailableResources(DayOfWeek.THURSDAY, TIME_12_OCLOCK);

        assertEquals(NUMBER_OF_CLASSROOMS, availableResources.size());

        assertTrue(availableResources.contains(classroomA));
        assertTrue(availableResources.contains(classroomB));
        assertTrue(availableResources.contains(classroomLarge));
    }

    @Test
    public void listAvailableResourcesShouldNotReturnBookedResources() {
        bookingSystem.bookResource(CLASSROOM_B_ID, DayOfWeek.FRIDAY, TIME_12_OCLOCK);

        Set<BookableResource> availableResources = bookingSystem.listAvailableResources(DayOfWeek.FRIDAY, TIME_12_OCLOCK);

        assertEquals(NUMBER_OF_CLASSROOMS - 1, availableResources.size());

        assertTrue(availableResources.contains(classroomA));
        assertTrue(availableResources.contains(classroomLarge));
    }

    @Test
    public void bookingUsingCapacityAndEquipmentShouldBePossible() {
        bookingSystem.bookResource(CAPACITY_12_PEOPLE, Equipment.PHONE, DayOfWeek.FRIDAY, TIME_12_OCLOCK);

        Set<BookableResource> availableResources = bookingSystem.listAvailableResources(DayOfWeek.FRIDAY, TIME_12_OCLOCK);

        assertEquals(NUMBER_OF_CLASSROOMS - 1, availableResources.size());

        assertTrue(availableResources.contains(classroomA));
        assertTrue(availableResources.contains(classroomB));
    }

    @Test
    public void bookingUsingCapacityAndSetOfEquipmentShouldBePossible() {
        Set<Equipment> equipment = new HashSet<>();
        equipment.add(Equipment.PHONE);
        bookingSystem.bookResource(CAPACITY_12_PEOPLE, equipment, DayOfWeek.FRIDAY, TIME_12_OCLOCK);

        Set<BookableResource> availableResources = bookingSystem.listAvailableResources(DayOfWeek.FRIDAY, TIME_12_OCLOCK);

        assertEquals(NUMBER_OF_CLASSROOMS - 1, availableResources.size());

        assertTrue(availableResources.contains(classroomA));
        assertTrue(availableResources.contains(classroomB));
    }

    @Test
    public void doubleBookingShouldNotBeAllowed() {
        bookingSystem.bookResource(CAPACITY_12_PEOPLE, Equipment.PHONE, DayOfWeek.FRIDAY, TIME_12_OCLOCK);

        assertThrows(IllegalArgumentException.class, () ->
                bookingSystem.bookResource(CLASSROOM_LARGE_ID, DayOfWeek.FRIDAY, TIME_12_OCLOCK));
    }

    @Test
    public void bookingShouldThrowIANForAmbiguousReservation() {
        assertThrows(IllegalArgumentException.class, () ->
                bookingSystem.bookResource(CAPACITY_12_PEOPLE, Equipment.WHITEBOARD, DayOfWeek.FRIDAY, TIME_12_OCLOCK));
    }

    @Test
    public void bookingWithSetShouldThrowIANForAmbiguousReservation() {
        Set<Equipment> equipment = new HashSet<>();
        equipment.add(Equipment.PROJECTOR);
        equipment.add(Equipment.WHITEBOARD);
        assertThrows(IllegalArgumentException.class, () -> bookingSystem.bookResource(CAPACITY_12_PEOPLE, equipment, DayOfWeek.FRIDAY, TIME_12_OCLOCK));
    }

    @Test
    public void bookingWithSetShouldThrowIAEForNonExistingResource() {
        Set<Equipment> equipment = new HashSet<>();
        equipment.add(Equipment.PROJECTOR);
        equipment.add(Equipment.WHITEBOARD);
        assertThrows(IllegalArgumentException.class, () -> bookingSystem.bookResource(CAPACITY_60_PEOPLE, equipment, DayOfWeek.FRIDAY, TIME_12_OCLOCK));
    }
}
