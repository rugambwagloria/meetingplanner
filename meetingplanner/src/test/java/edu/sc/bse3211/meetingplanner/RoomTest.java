package edu.sc.bse3211.meetingplanner;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class RoomTest {
    private Room room;
    private Meeting meeting;
    private ArrayList<Person> attendees;

    @Before
    public void setUp() {
        room = new Room("R101");
        meeting = new Meeting(3, 15, 9, 10, new ArrayList<Person>(), room, "Test Meeting");
    }

    @Test
    public void testDefaultConstructor() {
        Room defaultRoom = new Room();
        assertEquals("", defaultRoom.getID());
    }

    @Test
    public void testParameterizedConstructor() {
        assertEquals("R101", room.getID());
    }

    @Test
    public void testGetID() {
        assertEquals("R101", room.getID());
    }

    @Test
    public void testAddMeetingValidNoConflict() throws TimeConflictException {
        room.addMeeting(meeting);
        assertTrue(room.isBusy(3, 15, 9, 10));
    }

    @Test(expected = TimeConflictException.class)
    public void testAddMeetingOverlapThrowsWithRoomPrefix() throws TimeConflictException {
        room.addMeeting(meeting); // First meeting
        Meeting overlapping = new Meeting(3, 15, 9, 10, new ArrayList<Person>(), room, "Overlapping");
        room.addMeeting(overlapping); // Overlap
    }

    @Test
    public void testIsBusyFalseEmpty() throws TimeConflictException {
        assertFalse(room.isBusy(3, 15, 9, 10));
    }

    @Test
    public void testIsBusyTrueAfterAdd() throws TimeConflictException {
        room.addMeeting(meeting);
        assertTrue(room.isBusy(3, 15, 9, 10));
    }

    @Test
    public void testIsBusyFalseNonAdjacent() throws TimeConflictException {
        room.addMeeting(meeting); // 9-10
        assertFalse(room.isBusy(3, 15, 11, 12)); // After
        assertFalse(room.isBusy(3, 15, 7, 8)); // Before
    }

    @Test
    public void testPrintAgendaMonthContainsMeeting() throws TimeConflictException {
        room.addMeeting(meeting);
        String agenda = room.printAgenda(3, 15);
        assertTrue(agenda.contains("Test Meeting"));
    }

    @Test
    public void testPrintAgendaDayContainsMeeting() throws TimeConflictException {
        room.addMeeting(meeting);
        String agenda = room.printAgenda(3, 15);
        assertTrue(agenda.contains("Test Meeting"));
    }

    @Test
    public void testGetMeeting() throws TimeConflictException {
        room.addMeeting(meeting);
        Meeting retrieved = room.getMeeting(3, 15, 0);
        assertEquals("Test Meeting", retrieved.getDescription());
    }

    @Test
    public void testRemoveMeeting() throws TimeConflictException {
        room.addMeeting(meeting);
        room.removeMeeting(3, 15, 0);
        assertFalse(room.isBusy(3, 15, 9, 10));
    }
    // AVAILABILITY CHECKS — isBusy
    // ─────────────────────────────────────────

    @Test
    public void testIsBusy_NoMeetingsBooked_ReturnsFalse() throws TimeConflictException {
        assertFalse(
            "Room with no bookings should not be busy",
            room.isBusy(3, 15, 9, 10)
        );
    }

    @Test
    public void testIsBusy_AdjacentSlot_ReturnsFalse() throws TimeConflictException {
        Meeting m = new Meeting(3, 15, 9, 10, attendees, room, "Morning");
        room.addMeeting(m);

        assertFalse(
            "Room should be free in the slot immediately after a booking",
            room.isBusy(3, 15, 10, 11)
        );
    }

    @Test
    public void testIsBusy_DifferentDay_ReturnsFalse() throws TimeConflictException {
        Meeting m = new Meeting(3, 15, 9, 10, attendees, room, "Monday");
        room.addMeeting(m);

        assertFalse(
            "Room on a different day should not be affected by a booking",
            room.isBusy(3, 16, 9, 10)
        );
    }

}
