package edu.sc.bse3211.meetingplanner;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class PersonTest {
	// Add test methods here. 
    // You are not required to write tests for all classes.
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

    
}
