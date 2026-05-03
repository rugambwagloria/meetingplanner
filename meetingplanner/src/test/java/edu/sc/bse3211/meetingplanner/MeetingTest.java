package edu.sc.bse3211.meetingplanner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class MeetingTest {
    private Room room;
    private Person employee;
    private ArrayList<Person> attendees;
	@Test
    public void testAddMeeting_ValidMeeting_RoomIsNowBusy() throws TimeConflictException {
        Meeting m = new Meeting(3, 15, 9, 10, attendees, room, "Sprint Review");
        room.addMeeting(m);

        assertTrue(
            "Room should be busy after a meeting is added in that slot",
            room.isBusy(3, 15, 9, 10)
        );
    }

    @Test
    public void testAddMeeting_ValidMeeting_PersonIsNowBusy() throws TimeConflictException {
        Meeting m = new Meeting(3, 15, 9, 10, attendees, room, "Sprint Review");
        employee.addMeeting(m);

        assertTrue(
            "Employee should be busy after a meeting is added in that slot",
            employee.isBusy(3, 15, 9, 10)
        );
    }

    @Test
    public void testAddMeeting_MeetingAppearsInRoomAgenda() throws TimeConflictException {
        Meeting m = new Meeting(3, 15, 9, 10, attendees, room, "Board Meeting");
        room.addMeeting(m);

        String agenda = room.printAgenda(3, 15);
        assertTrue(
            "Room agenda should list the meeting description after booking",
            agenda.contains("Board Meeting")
        );
    }

    // ─────────────────────────────────────────
    // 2. DOUBLE BOOKING — conflict detection
    // ─────────────────────────────────────────

    @Test(expected = TimeConflictException.class)
    public void testAddMeeting_DoubleBookRoom_ThrowsTimeConflictException()
            throws TimeConflictException {
        Meeting first  = new Meeting(3, 15, 9, 10, attendees, room, "First");
        Meeting second = new Meeting(3, 15, 9, 10, attendees, room, "Second");

        room.addMeeting(first);   // must succeed
        room.addMeeting(second);  // must throw TimeConflictException
    }

    @Test(expected = TimeConflictException.class)
    public void testAddMeeting_DoubleBookPerson_ThrowsTimeConflictException()
            throws TimeConflictException {
        Meeting first  = new Meeting(3, 15, 9, 10, attendees, room, "First");
        Meeting second = new Meeting(3, 15, 9, 10, attendees, room, "Conflict");

        employee.addMeeting(first);
        employee.addMeeting(second); // must throw TimeConflictException
    }

    // ─────────────────────────────────────────
    // 3. INVALID DATES — e.g. February 35th
    // ─────────────────────────────────────────

    @Test(expected = TimeConflictException.class)
    public void testAddMeeting_February35th_ThrowsTimeConflictException()
            throws TimeConflictException {
        Meeting m = new Meeting(2, 35, 9, 10, attendees, room, "Impossible");
        room.addMeeting(m);
    }

    @Test(expected = TimeConflictException.class)
    public void testAddMeeting_Month13_ThrowsTimeConflictException()
            throws TimeConflictException {
        Meeting m = new Meeting(13, 1, 9, 10, attendees, room, "Bad Month");
        room.addMeeting(m);
    }

    @Test(expected = TimeConflictException.class)
    public void testAddMeeting_Day0_ThrowsTimeConflictException()
            throws TimeConflictException {
        Meeting m = new Meeting(3, 0, 9, 10, attendees, room, "Day Zero");
        room.addMeeting(m);
    }


    // ─────────────────────────────────────────
    // 4. INVALID TIMES
    // ─────────────────────────────────────────

    @Test(expected = TimeConflictException.class)
    public void testAddMeeting_EndTimeBeforeStartTime_ThrowsTimeConflictException()
            throws TimeConflictException {
        Meeting m = new Meeting(3, 15, 10, 8, attendees, room, "Time Warp");
        room.addMeeting(m);
    }

    @Test(expected = TimeConflictException.class)
    public void testAddMeeting_StartEqualsEndTime_ThrowsTimeConflictException()
            throws TimeConflictException {
        Meeting m = new Meeting(3, 15, 9, 9, attendees, room, "Zero Length");
        room.addMeeting(m);
    }

    @Test(expected = TimeConflictException.class)
    public void testAddMeeting_HourOutsideRange_ThrowsTimeConflictException()
            throws TimeConflictException {
        Meeting m = new Meeting(3, 15, 25, 26, attendees, room, "Invalid Hour");
        room.addMeeting(m);
    }


    // ─────────────────────────────────────────
    // M-01 — Meeting: Constructor / Data Integrity
    // ─────────────────────────────────────────

    /**
     * All fields passed to the Meeting constructor must be stored and
     * accessible via the corresponding getters.
     */
    @Test
    public void testMeeting_Constructor_AllFieldsSetCorrectly()
            throws TimeConflictException {
        Meeting m = new Meeting(4, 20, 14, 15, attendees, room, "Data Integrity Check");

        assertEquals("Month should be 4",               4,    m.getMonth());
        assertEquals("Day should be 20",               20,    m.getDay());
        assertEquals("Start hour should be 14",        14,    m.getStartTime());
        assertEquals("End hour should be 15",          15,    m.getEndTime());
        assertEquals("Description should match",       "Data Integrity Check", m.getDescription());
        assertEquals("Room should match",              room,  m.getRoom());
        assertFalse("Attendee list should not be empty", m.getAttendees().isEmpty());
    }

    // ─────────────────────────────────────────
    // M-02 — Meeting: addAttendee increases list size
    // ─────────────────────────────────────────

    /**
     *  Calling addAttendee with a valid Person must increase the
     * attendee list size by one.
     */
    @Test
    public void testMeeting_AddAttendee_ListSizeIncreases()
            throws TimeConflictException {
        Meeting m = new Meeting(3, 15, 9, 10, attendees, room, "Attendee Test");
        int sizeBefore = m.getAttendees().size();

        Person newPerson = new Person("Bob");
        m.addAttendee(newPerson);

        assertEquals(
            "Attendee list size should grow by exactly 1 after addAttendee",
            sizeBefore + 1,
            m.getAttendees().size()
        );
    }

    // ─────────────────────────────────────────
    // M-03 — Meeting: Null Room
    // ─────────────────────────────────────────

    /**
     * Passing null as the room to the Meeting constructor (or
     * addMeeting) should throw an exception.
     */
    @Test(expected = Exception.class)
    public void testMeeting_NullRoom_ThrowsException() throws Exception {
        // Passing null room — implementation must reject this
        Meeting m = new Meeting(3, 15, 9, 10, attendees, null, "Null Room");
        room.addMeeting(m);
    }

    // ─────────────────────────────────────────
    // M-05 — Meeting: Duplicate Attendee
    // ─────────────────────────────────────────

    /**
     *  Adding the same Person instance twice must not produce a
     * duplicate entry in the attendee list.
     */
    @Test
    public void testMeeting_DuplicateAttendee_NoduplicateInList()
            throws TimeConflictException {
        Meeting m = new Meeting(3, 15, 9, 10, attendees, room, "Duplicate Test");
        m.addAttendee(employee); // add the same person again

        long count = m.getAttendees().stream()
                       .filter(p -> p.equals(employee))
                       .count();

        assertEquals(
            "The same person should appear only once in the attendee list",
            1,
            count
        );
    }
    
    // ─────────────────────────────────────────
    // A-02 — Agenda: Empty Room Agenda
    // ─────────────────────────────────────────

    /**
     * A-02: Printing the agenda for a room with no meetings should return
     * a non-null, non-empty string (e.g. a header) — not crash or return null.
     */
    @Test
    public void testPrintAgenda_EmptyRoom_ReturnsEmptyOrHeader()
            throws TimeConflictException {
        String agenda = room.printAgenda(3, 15);
        assertNotNull("Agenda for an empty room must not be null", agenda);
        // The string may just be a header/date line — that is fine
    }

    // ─────────────────────────────────────────
    // A-03 — Agenda: Person Agenda Has Meetings
    // ─────────────────────────────────────────

    /**
     * A-03: A person with two booked meetings must have both descriptions
     * appear in their printed agenda.
     */
    @Test
    public void testPrintPersonAgenda_MultipleMeetings_AllAppear()
            throws TimeConflictException {
        Meeting m1 = new Meeting(3, 15, 9,  10, attendees, room, "Person Alpha");
        Meeting m2 = new Meeting(3, 15, 11, 12, attendees, room, "Person Beta");
        employee.addMeeting(m1);
        employee.addMeeting(m2);

        String agenda = employee.printAgenda(3, 15);
        assertTrue("Alpha meeting should appear in person agenda", agenda.contains("Person Alpha"));
        assertTrue("Beta meeting should appear in person agenda",  agenda.contains("Person Beta"));
    }

    // ─────────────────────────────────────────
    // A-04 — Agenda: Null Agenda Request
    // ─────────────────────────────────────────

    /**
     * A-04: Passing an out-of-range or null-equivalent date to printAgenda
     * must throw an exception rather than return garbage output.
     */
    @Test(expected = Exception.class)
    public void testPrintAgenda_InvalidMonth_ThrowsException() throws Exception {
        // Month 0 is invalid — implementation should throw
        room.printAgenda(0, 1);
    }

    @Test(expected = Exception.class)
    public void testPrintAgenda_InvalidDay_ThrowsException() throws Exception {
        // Day 0 is invalid — implementation should throw
        room.printAgenda(3, 0);
    }
}
