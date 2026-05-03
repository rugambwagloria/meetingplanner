package edu.sc.bse3211.meetingplanner;

import java.util.ArrayList;

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
}
