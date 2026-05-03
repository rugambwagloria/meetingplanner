package edu.sc.bse3211.meetingplanner;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

public class CalendarTest {
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
	public void testAddMeeting_holiday() {
		// Create Janan Luwum holiday
		Calendar calendar = new Calendar();
		// Add to calendar object.
		try {
			Meeting janan = new Meeting(2, 16, "Janan Luwum");
			calendar.addMeeting(janan);
			// Verify that it was added.
			Boolean added = calendar.isBusy(2, 16, 0, 23);
			assertTrue("Janan Luwum Day should be marked as busy on the calendar",added);
		} catch(TimeConflictException e) {
			fail("Should not throw exception: " + e.getMessage());
		}
	}

	@Test
	public void testAddMeeting_invalidMonthThrowsException() {
		Calendar calendar = new Calendar();
		try {
			calendar.addMeeting(new Meeting(13, 15, 10, 11));
			fail("Expected TimeConflictException for month 13");
		} catch(TimeConflictException e) {
			assertTrue(e.getMessage().contains("Month does not exist"));
		}
	}

	@Test
	public void testAddMeeting_invalidDayForThirtyDayMonthThrowsException() {
		Calendar calendar = new Calendar();
		try {
			calendar.addMeeting(new Meeting(4, 31, 10, 11));
			fail("Expected TimeConflictException for April 31");
		} catch(TimeConflictException e) {
			assertTrue(e.getMessage().contains("Day does not exist for month 4"));
		}
	}

	@Test
	public void testAddMeeting_invalidDayForFebruaryThrowsException() {
		Calendar calendar = new Calendar();
		try {
			calendar.addMeeting(new Meeting(2, 29, 10, 11));
			fail("Expected TimeConflictException for February 29");
		} catch(TimeConflictException e) {
			assertTrue(e.getMessage().contains("Day does not exist for month 2"));
		}
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

	   /**
     * C-04: After removing a meeting the room should no longer be busy in
     * the slot that was previously occupied.
     */
    @Test
    public void testCalendar_RemoveMeeting_RoomBecomesAvailable()
            throws TimeConflictException {
        Meeting m = new Meeting(3, 15, 9, 10, attendees, room, "Removable Meeting");
        room.addMeeting(m);

        // Confirm it is busy before removal
        assertTrue("Room should be busy before removal", room.isBusy(3, 15, 9, 10));

        room.removeMeeting(3, 15, 0);

        assertFalse(
            "Room should be free after the meeting has been removed",
            room.isBusy(3, 15, 9, 10)
        );
    }

    // ─────────────────────────────────────────
    // C-07 — Calendar: Partial Overlap
    // ─────────────────────────────────────────

    /**
     *A meeting from 10-12 that partially overlaps a booked 9-11 slot
     * must throw TimeConflictException.
     */
    @Test(expected = TimeConflictException.class)
    public void testCalendar_PartialOverlap_ThrowsTimeConflictException()
            throws TimeConflictException {
        Meeting first  = new Meeting(3, 15,  9, 11, attendees, room, "First");
        Meeting second = new Meeting(3, 15, 10, 12, attendees, room, "Overlapping");

        room.addMeeting(first);
        room.addMeeting(second); // overlaps 10-11 — must throw
    }

	/* Two meetings with a gap between them (9-10 and 11-12) must both
     * succeed — there is no overlap, so no exception should be thrown.
     */
    @Test
    public void testCalendar_GapBetweenMeetings_NoExceptionThrown()
            throws TimeConflictException {
        Meeting m1 = new Meeting(3, 15, 9,  10, attendees, room, "Morning Stand-up");
        Meeting m2 = new Meeting(3, 15, 11, 12, attendees, room, "Afternoon Sync");

        room.addMeeting(m1);
        room.addMeeting(m2); // must NOT throw — gap exists between 10 and 11
    }


}
