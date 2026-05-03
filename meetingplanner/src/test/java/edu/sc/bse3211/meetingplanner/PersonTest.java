package edu.sc.bse3211.meetingplanner;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class PersonTest {
	
    private Room room;
    private Person employee;
    private ArrayList<Person> attendees;

    @Before
    public void setUp() {
        // Runs before EACH test — fresh state every time
        room = new Room("ROOM-A");
        employee = new Person("Alice");
        attendees = new ArrayList<>();
        attendees.add(employee);
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
    @Test(expected = TimeConflictException.class)
    public void testAddMeeting_DoubleBookPerson_ThrowsTimeConflictException()
            throws TimeConflictException {
        Meeting first  = new Meeting(3, 15, 9, 10, attendees, room, "First");
        Meeting second = new Meeting(3, 15, 9, 10, attendees, room, "Conflict");

        employee.addMeeting(first);
        employee.addMeeting(second); // must throw TimeConflictException
    }

     // ─────────────────────────────────────────
    // AGENDA PRINTING
    // ─────────────────────────────────────────

    @Test
    public void testPrintAgenda_ReturnsNonNull() throws TimeConflictException {
        String agenda = room.printAgenda(3, 15);
        assertNotNull("printAgenda should never return null", agenda);
    }

    @Test
    public void testPrintAgenda_MultipleBookings_AllAppearInOutput()
            throws TimeConflictException {
        Meeting m1 = new Meeting(3, 15, 9,  10, attendees, room, "Meeting Alpha");
        Meeting m2 = new Meeting(3, 15, 11, 12, attendees, room, "Meeting Beta");
        room.addMeeting(m1);
        room.addMeeting(m2);

        String agenda = room.printAgenda(3, 15);
        assertTrue("Alpha should appear in agenda", agenda.contains("Meeting Alpha"));
        assertTrue("Beta should appear in agenda",  agenda.contains("Meeting Beta"));
    }
    // ─────────────────────────────────────────
     // PR-01 — Person(Room): Delegation to Calendar
     // ─────────────────────────────────────────

     /**    * PR-01: Calling person.addMeeting() must delegate to the underlying
       //Calendar so that the person is subsequently marked as busy.*/
     
    @Test
    public void testPerson_AddMeeting_DelegatesToCalendar()
            throws TimeConflictException {
        Meeting m = new Meeting(5, 10, 14, 15, attendees, room, "Delegation Check");
        employee.addMeeting(m);

        assertTrue(
            "Person.addMeeting should delegate to calendar — person must be busy afterwards",
            employee.isBusy(5, 10, 14, 15)
        );
    }

    /**
     * PR-01 (Room variant): Calling room.addMeeting() must delegate to the
     * underlying Calendar so that the room is subsequently marked as busy.
     */
    @Test
    public void testRoom_AddMeeting_DelegatesToCalendar()
            throws TimeConflictException {
        Meeting m = new Meeting(5, 10, 14, 15, attendees, room, "Room Delegation Check");
        room.addMeeting(m);

        assertTrue(
            "Room.addMeeting should delegate to calendar — room must be busy afterwards",
            room.isBusy(5, 10, 14, 15)
        );
    }


    // PR-05 — Person/Room: Null check
    

    /**
     * PR-05: Passing null as the Meeting to addMeeting must throw an
     * exception rather than silently succeed.
     */
    @Test(expected = Exception.class)
    public void testPerson_AddMeeting_NullMeeting_ThrowsException() throws Exception {
        employee.addMeeting(null);
    }

    @Test(expected = Exception.class)
    public void testRoom_AddMeeting_NullMeeting_ThrowsException() throws Exception {
        room.addMeeting(null);
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
