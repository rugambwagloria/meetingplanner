package edu.sc.bse3211.meetingplanner;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class MeetingPlannerTest {

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

    // ─────────────────────────────────────────
    // 1. BOOKING A MEETING — normal flow
    // ─────────────────────────────────────────

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
    // 5. AVAILABILITY CHECKS — isBusy
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

    // ─────────────────────────────────────────
    // 6. VACATION BOOKING
    // ─────────────────────────────────────────

    @Test
    public void testVacation_PersonBusyDuringVacationDay() throws TimeConflictException {
        Meeting vacationDay = new Meeting(3, 20, 0, 23, attendees, new Room(), "vacation");
        employee.addMeeting(vacationDay);

        assertTrue(
            "Employee on vacation should be busy during any time slot that day",
            employee.isBusy(3, 20, 9, 10)
        );
    }

    @Test(expected = TimeConflictException.class)
    public void testVacation_ConflictsWithExistingMeeting_ThrowsTimeConflictException()
            throws TimeConflictException {
        Meeting existing = new Meeting(3, 22, 10, 11, attendees, room, "Important Meeting");
        employee.addMeeting(existing);

        // All-day vacation on a day that already has a meeting must conflict
        Meeting vacationConflict = new Meeting(3, 22, 0, 23, attendees, new Room(), "vacation");
        employee.addMeeting(vacationConflict);
    }

    // ─────────────────────────────────────────
    // 7. ORGANIZATION — getRoom / getEmployee
    // ─────────────────────────────────────────

    @Test(expected = Exception.class)
    public void testOrganization_GetRoom_InvalidID_ThrowsException() throws Exception {
        Organization org = new Organization();
        org.getRoom("NONEXISTENT-ROOM-XYZ");
    }

    @Test(expected = Exception.class)
    public void testOrganization_GetEmployee_InvalidName_ThrowsException() throws Exception {
        Organization org = new Organization();
        org.getEmployee("Ghost Person Who Does Not Exist");
    }

    // ─────────────────────────────────────────
    // 8. AGENDA PRINTING
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