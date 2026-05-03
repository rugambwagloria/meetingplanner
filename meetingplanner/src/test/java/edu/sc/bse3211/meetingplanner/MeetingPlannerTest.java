// package edu.sc.bse3211.meetingplanner;

// import java.util.ArrayList;

// import static org.junit.Assert.assertEquals;
// import static org.junit.Assert.assertFalse;
// import static org.junit.Assert.assertNotNull;
// import static org.junit.Assert.assertTrue;
// import org.junit.Before;
// import org.junit.Test;

// public class MeetingPlannerTest {

//     private Room room;
//     private Person employee;
//     private ArrayList<Person> attendees;

//     @Before
//     public void setUp() {
//         // Runs before EACH test — fresh state every time
//         room = new Room("ROOM-A");
//         employee = new Person("Alice");
//         attendees = new ArrayList<>();
//         attendees.add(employee);
//     }

//     // ─────────────────────────────────────────
//     // 1. BOOKING A MEETING --(normal flow)
//     // ─────────────────────────────────────────

//     @Test
//     public void testAddMeeting_ValidMeeting_RoomIsNowBusy() throws TimeConflictException {
//         Meeting m = new Meeting(3, 15, 9, 10, attendees, room, "Sprint Review");
//         room.addMeeting(m);

//         assertTrue(
//             "Room should be busy after a meeting is added in that slot",
//             room.isBusy(3, 15, 9, 10)
//         );
//     }

//     @Test
//     public void testAddMeeting_ValidMeeting_PersonIsNowBusy() throws TimeConflictException {
//         Meeting m = new Meeting(3, 15, 9, 10, attendees, room, "Sprint Review");
//         employee.addMeeting(m);

//         assertTrue(
//             "Employee should be busy after a meeting is added in that slot",
//             employee.isBusy(3, 15, 9, 10)
//         );
//     }

//     @Test
//     public void testAddMeeting_MeetingAppearsInRoomAgenda() throws TimeConflictException {
//         Meeting m = new Meeting(3, 15, 9, 10, attendees, room, "Board Meeting");
//         room.addMeeting(m);

//         String agenda = room.printAgenda(3, 15);
//         assertTrue(
//             "Room agenda should list the meeting description after booking",
//             agenda.contains("Board Meeting")
//         );
//     }

//     // ─────────────────────────────────────────
//     // 2. DOUBLE BOOKING — conflict detection
//     // ─────────────────────────────────────────

//     @Test(expected = TimeConflictException.class)
//     public void testAddMeeting_DoubleBookRoom_ThrowsTimeConflictException()
//             throws TimeConflictException {
//         Meeting first  = new Meeting(3, 15, 9, 10, attendees, room, "First");
//         Meeting second = new Meeting(3, 15, 9, 10, attendees, room, "Second");

//         room.addMeeting(first);   // must succeed
//         room.addMeeting(second);  // must throw TimeConflictException
//     }

//     @Test(expected = TimeConflictException.class)
//     public void testAddMeeting_DoubleBookPerson_ThrowsTimeConflictException()
//             throws TimeConflictException {
//         Meeting first  = new Meeting(3, 15, 9, 10, attendees, room, "First");
//         Meeting second = new Meeting(3, 15, 9, 10, attendees, room, "Conflict");

//         employee.addMeeting(first);
//         employee.addMeeting(second); // must throw TimeConflictException
//     }

//     // ─────────────────────────────────────────
//     // 3. INVALID DATES — e.g. February 35th
//     // ─────────────────────────────────────────

//     @Test(expected = TimeConflictException.class)
//     public void testAddMeeting_February35th_ThrowsTimeConflictException()
//             throws TimeConflictException {
//         Meeting m = new Meeting(2, 35, 9, 10, attendees, room, "Impossible");
//         room.addMeeting(m);
//     }

//     @Test(expected = TimeConflictException.class)
//     public void testAddMeeting_Month13_ThrowsTimeConflictException()
//             throws TimeConflictException {
//         Meeting m = new Meeting(13, 1, 9, 10, attendees, room, "Bad Month");
//         room.addMeeting(m);
//     }

//     @Test(expected = TimeConflictException.class)
//     public void testAddMeeting_Day0_ThrowsTimeConflictException()
//             throws TimeConflictException {
//         Meeting m = new Meeting(3, 0, 9, 10, attendees, room, "Day Zero");
//         room.addMeeting(m);
//     }

//     // ─────────────────────────────────────────
//     // 4. INVALID TIMES
//     // ─────────────────────────────────────────

//     @Test(expected = TimeConflictException.class)
//     public void testAddMeeting_EndTimeBeforeStartTime_ThrowsTimeConflictException()
//             throws TimeConflictException {
//         Meeting m = new Meeting(3, 15, 10, 8, attendees, room, "Time Warp");
//         room.addMeeting(m);
//     }

//     @Test(expected = TimeConflictException.class)
//     public void testAddMeeting_StartEqualsEndTime_ThrowsTimeConflictException()
//             throws TimeConflictException {
//         Meeting m = new Meeting(3, 15, 9, 9, attendees, room, "Zero Length");
//         room.addMeeting(m);
//     }

//     @Test(expected = TimeConflictException.class)
//     public void testAddMeeting_HourOutsideRange_ThrowsTimeConflictException()
//             throws TimeConflictException {
//         Meeting m = new Meeting(3, 15, 25, 26, attendees, room, "Invalid Hour");
//         room.addMeeting(m);
//     }

//     // ─────────────────────────────────────────
//     // 5. AVAILABILITY CHECKS — isBusy
//     // ─────────────────────────────────────────

//     @Test
//     public void testIsBusy_NoMeetingsBooked_ReturnsFalse() throws TimeConflictException {
//         assertFalse(
//             "Room with no bookings should not be busy",
//             room.isBusy(3, 15, 9, 10)
//         );
//     }

//     @Test
//     public void testIsBusy_AdjacentSlot_ReturnsFalse() throws TimeConflictException {
//         Meeting m = new Meeting(3, 15, 9, 10, attendees, room, "Morning");
//         room.addMeeting(m);

//         assertFalse(
//             "Room should be free in the slot immediately after a booking",
//             room.isBusy(3, 15, 10, 11)
//         );
//     }

//     @Test
//     public void testIsBusy_DifferentDay_ReturnsFalse() throws TimeConflictException {
//         Meeting m = new Meeting(3, 15, 9, 10, attendees, room, "Monday");
//         room.addMeeting(m);

//         assertFalse(
//             "Room on a different day should not be affected by a booking",
//             room.isBusy(3, 16, 9, 10)
//         );
//     }

//     // ─────────────────────────────────────────
//     // 6. VACATION BOOKING
//     // ─────────────────────────────────────────

//     @Test
//     public void testVacation_PersonBusyDuringVacationDay() throws TimeConflictException {
//         Meeting vacationDay = new Meeting(3, 20, 0, 23, attendees, new Room(), "vacation");
//         employee.addMeeting(vacationDay);

//         assertTrue(
//             "Employee on vacation should be busy during any time slot that day",
//             employee.isBusy(3, 20, 9, 10)
//         );
//     }

//     @Test(expected = TimeConflictException.class)
//     public void testVacation_ConflictsWithExistingMeeting_ThrowsTimeConflictException()
//             throws TimeConflictException {
//         Meeting existing = new Meeting(3, 22, 10, 11, attendees, room, "Important Meeting");
//         employee.addMeeting(existing);

//         // All-day vacation on a day that already has a meeting must conflict
//         Meeting vacationConflict = new Meeting(3, 22, 0, 23, attendees, new Room(), "vacation");
//         employee.addMeeting(vacationConflict);
//     }

//     // ─────────────────────────────────────────
//     // 7. ORGANIZATION — getRoom / getEmployee
//     // ─────────────────────────────────────────

//     @Test(expected = Exception.class)
//     public void testOrganization_GetRoom_InvalidID_ThrowsException() throws Exception {
//         Organization org = new Organization();
//         org.getRoom("NONEXISTENT-ROOM-XYZ");
//     }

//     @Test(expected = Exception.class)
//     public void testOrganization_GetEmployee_InvalidName_ThrowsException() throws Exception {
//         Organization org = new Organization();
//         org.getEmployee("Ghost Person Who Does Not Exist");
//     }

//     // ─────────────────────────────────────────
//     // 8. AGENDA PRINTING
//     // ─────────────────────────────────────────

//     @Test
//     public void testPrintAgenda_ReturnsNonNull() throws TimeConflictException {
//         String agenda = room.printAgenda(3, 15);
//         assertNotNull("printAgenda should never return null", agenda);
//     }

//     @Test
//     public void testPrintAgenda_MultipleBookings_AllAppearInOutput()
//             throws TimeConflictException {
//         Meeting m1 = new Meeting(3, 15, 9,  10, attendees, room, "Meeting Alpha");
//         Meeting m2 = new Meeting(3, 15, 11, 12, attendees, room, "Meeting Beta");
//         room.addMeeting(m1);
//         room.addMeeting(m2);

//         String agenda = room.printAgenda(3, 15);
//         assertTrue("Alpha should appear in agenda", agenda.contains("Meeting Alpha"));
//         assertTrue("Beta should appear in agenda",  agenda.contains("Meeting Beta"));
//     }

    
//     // ─────────────────────────────────────────
//     // C-02 — Calendar: Gap Logic (no conflict)
//     // ─────────────────────────────────────────

//     /**
//      *  Two meetings with a gap between them (9-10 and 11-12) must both
//      * succeed — there is no overlap, so no exception should be thrown.
//      */
//     @Test
//     public void testCalendar_GapBetweenMeetings_NoExceptionThrown()
//             throws TimeConflictException {
//         Meeting m1 = new Meeting(3, 15, 9,  10, attendees, room, "Morning Stand-up");
//         Meeting m2 = new Meeting(3, 15, 11, 12, attendees, room, "Afternoon Sync");

//         room.addMeeting(m1);
//         room.addMeeting(m2); // must NOT throw — gap exists between 10 and 11
//     }

//     // ─────────────────────────────────────────
//     // C-03 — Calendar: Retrieval of a Meeting
//     // ─────────────────────────────────────────

//     /**
//      *  After adding a meeting, retrieving it by date/index must return
//      * the correct Meeting object (non-null, matching description).
//      */
//     @Test
//     public void testCalendar_GetMeeting_ReturnsCorrectMeeting()
//             throws TimeConflictException {
//         Meeting m = new Meeting(3, 15, 9, 10, attendees, room, "Retrieval Test");
//         room.addMeeting(m);

//         Meeting retrieved = room.getMeeting(3, 15, 0);
//         assertNotNull("getMeeting should return a non-null Meeting object", retrieved);
//         assertEquals(
//             "Retrieved meeting description should match the one that was added",
//             "Retrieval Test",
//             retrieved.getDescription()
//         );
//     }

//     // ─────────────────────────────────────────
//     // C-04 — Calendar: Removal of a Meeting
//     // ─────────────────────────────────────────

//     /**
//      * After removing a meeting the room should no longer be busy in
//      * the slot that was previously occupied.
//      */
//     @Test
//     public void testCalendar_RemoveMeeting_RoomBecomesAvailable()
//             throws TimeConflictException {
//         Meeting m = new Meeting(3, 15, 9, 10, attendees, room, "Removable Meeting");
//         room.addMeeting(m);

//         // Confirm it is busy before removal
//         assertTrue("Room should be busy before removal", room.isBusy(3, 15, 9, 10));

//         room.removeMeeting(3, 15, 0);

//         assertFalse(
//             "Room should be free after the meeting has been removed",
//             room.isBusy(3, 15, 9, 10)
//         );
//     }

//     // ─────────────────────────────────────────
//     // C-07 — Calendar: Partial Overlap
//     // ─────────────────────────────────────────

//     /**
//      *  A meeting from 10-12 that partially overlaps a booked 9-11 slot
//      * must throw TimeConflictException.
//      */
//     @Test(expected = TimeConflictException.class)
//     public void testCalendar_PartialOverlap_ThrowsTimeConflictException()
//             throws TimeConflictException {
//         Meeting first  = new Meeting(3, 15,  9, 11, attendees, room, "First");
//         Meeting second = new Meeting(3, 15, 10, 12, attendees, room, "Overlapping");

//         room.addMeeting(first);
//         room.addMeeting(second); // overlaps 10-11 — must throw
//     }

//     // ─────────────────────────────────────────
//     // M-01 — Meeting: Constructor / Data Integrity
//     // ─────────────────────────────────────────

//     /**
//      * All fields passed to the Meeting constructor must be stored and
//      * accessible via the corresponding getters.
//      */
//     @Test
//     public void testMeeting_Constructor_AllFieldsSetCorrectly()
//             throws TimeConflictException {
//         Meeting m = new Meeting(4, 20, 14, 15, attendees, room, "Data Integrity Check");

//         assertEquals("Month should be 4",               4,    m.getMonth());
//         assertEquals("Day should be 20",               20,    m.getDay());
//         assertEquals("Start hour should be 14",        14,    m.getStartTime());
//         assertEquals("End hour should be 15",          15,    m.getEndTime());
//         assertEquals("Description should match",       "Data Integrity Check", m.getDescription());
//         assertEquals("Room should match",              room,  m.getRoom());
//         assertFalse("Attendee list should not be empty", m.getAttendees().isEmpty());
//     }

//     // ─────────────────────────────────────────
//     // M-02 — Meeting: addAttendee increases list size
//     // ─────────────────────────────────────────

//     /**
//      *  Calling addAttendee with a valid Person must increase the
//      * attendee list size by one.
//      */
//     @Test
//     public void testMeeting_AddAttendee_ListSizeIncreases()
//             throws TimeConflictException {
//         Meeting m = new Meeting(3, 15, 9, 10, attendees, room, "Attendee Test");
//         int sizeBefore = m.getAttendees().size();

//         Person newPerson = new Person("Bob");
//         m.addAttendee(newPerson);

//         assertEquals(
//             "Attendee list size should grow by exactly 1 after addAttendee",
//             sizeBefore + 1,
//             m.getAttendees().size()
//         );
//     }

//     // ─────────────────────────────────────────
//     // M-03 — Meeting: Null Room
//     // ─────────────────────────────────────────

//     /**
//      * Passing null as the room to the Meeting constructor (or
//      * addMeeting) should throw an exception.
//      */
//     @Test(expected = Exception.class)
//     public void testMeeting_NullRoom_ThrowsException() throws Exception {
//         // Passing null room — implementation must reject this
//         Meeting m = new Meeting(3, 15, 9, 10, attendees, null, "Null Room");
//         room.addMeeting(m);
//     }

//     // ─────────────────────────────────────────
//     // M-05 — Meeting: Duplicate Attendee
//     // ─────────────────────────────────────────

//     /**
//      *  Adding the same Person instance twice must not produce a
//      * duplicate entry in the attendee list.
//      */
//     @Test
//     public void testMeeting_DuplicateAttendee_NoduplicateInList()
//             throws TimeConflictException {
//         Meeting m = new Meeting(3, 15, 9, 10, attendees, room, "Duplicate Test");
//         m.addAttendee(employee); // add the same person again

//         long count = m.getAttendees().stream()
//                        .filter(p -> p.equals(employee))
//                        .count();

//         assertEquals(
//             "The same person should appear only once in the attendee list",
//             1,
//             count
//         );
//     }

//     // ─────────────────────────────────────────
//     // O-01 — Organization: Valid Search
//     // ─────────────────────────────────────────

//     /**
//      *  Searching the Organization for a room ID or employee name that
//      * exists must return a non-null object.
//      */
//     @Test
//     public void testOrganization_GetRoom_ValidID_ReturnsRoom() throws Exception {
//         Organization org = new Organization();
//         // Use a room ID known to exist in the default Organization setup
//         Room found = org.getRoom("LLT6A");
//         assertNotNull("getRoom with a valid ID should return a non-null Room", found);
//     }

//     @Test
//     public void testOrganization_GetEmployee_ValidName_ReturnsPerson() throws Exception {
//         Organization org = new Organization();
//         // Use an employee name known to exist in the default Organization setup
//         Person found = org.getEmployee("Marcellus");
//         assertNotNull("getEmployee with a valid name should return a non-null Person", found);
//     }

//     // ─────────────────────────────────────────
//     // O-03 — Organization: Null Search
//     // ─────────────────────────────────────────

//     /**
//      * O-03: Passing null as the search key to getRoom or getEmployee must
//      * throw an exception rather than return silently.
//      */
//     @Test(expected = Exception.class)
//     public void testOrganization_GetRoom_NullID_ThrowsException() throws Exception {
//         Organization org = new Organization();
//         org.getRoom(null);
//     }

//     @Test(expected = Exception.class)
//     public void testOrganization_GetEmployee_NullName_ThrowsException() throws Exception {
//         Organization org = new Organization();
//         org.getEmployee(null);
//     }

//     // ─────────────────────────────────────────
//     // O-04 — Organization: Empty Organization
//     // ─────────────────────────────────────────

//     /**
//      * O-04: Searching in an Organization that has no rooms/employees should
//      * return null or an empty result (and must not crash).
//      */
//     @Test
//     public void testOrganization_EmptyOrg_GetRoom_ReturnsNullOrEmpty() {
//         // Create a fresh, unpopulated Organization (no rooms added)
//         Organization emptyOrg = new Organization();
//         try {
//             Room result = emptyOrg.getRoom("ANY-ROOM");
//             // Implementation may return null for an empty org
//             // (it may also throw — both are acceptable behaviours)
//         } catch (Exception e) {
//             // Throwing is also acceptable — just must not crash the JVM
//         }
//     }

//     // ─────────────────────────────────────────
//     // PR-01 — Person/Room: Delegation to Calendar
//     // ─────────────────────────────────────────

//     /**
//      * PR-01: Calling person.addMeeting() must delegate to the underlying
//      * Calendar so that the person is subsequently marked as busy.
//      */
//     @Test
//     public void testPerson_AddMeeting_DelegatesToCalendar()
//             throws TimeConflictException {
//         Meeting m = new Meeting(5, 10, 14, 15, attendees, room, "Delegation Check");
//         employee.addMeeting(m);

//         assertTrue(
//             "Person.addMeeting should delegate to calendar — person must be busy afterwards",
//             employee.isBusy(5, 10, 14, 15)
//         );
//     }

//     /**
//      * PR-01 (Room variant): Calling room.addMeeting() must delegate to the
//      * underlying Calendar so that the room is subsequently marked as busy.
//      */
//     @Test
//     public void testRoom_AddMeeting_DelegatesToCalendar()
//             throws TimeConflictException {
//         Meeting m = new Meeting(5, 10, 14, 15, attendees, room, "Room Delegation Check");
//         room.addMeeting(m);

//         assertTrue(
//             "Room.addMeeting should delegate to calendar — room must be busy afterwards",
//             room.isBusy(5, 10, 14, 15)
//         );
//     }

//     // ─────────────────────────────────────────
//     // PR-05 — Person/Room: Null check
//     // ─────────────────────────────────────────

//     /**
//      * PR-05: Passing null as the Meeting to addMeeting must throw an
//      * exception rather than silently succeed.
//      */
//     @Test(expected = Exception.class)
//     public void testPerson_AddMeeting_NullMeeting_ThrowsException() throws Exception {
//         employee.addMeeting(null);
//     }

//     @Test(expected = Exception.class)
//     public void testRoom_AddMeeting_NullMeeting_ThrowsException() throws Exception {
//         room.addMeeting(null);
//     }

//     // ─────────────────────────────────────────
//     // A-02 — Agenda: Empty Room Agenda
//     // ─────────────────────────────────────────

//     /**
//      * A-02: Printing the agenda for a room with no meetings should return
//      * a non-null, non-empty string (e.g. a header) — not crash or return null.
//      */
//     @Test
//     public void testPrintAgenda_EmptyRoom_ReturnsEmptyOrHeader()
//             throws TimeConflictException {
//         String agenda = room.printAgenda(3, 15);
//         assertNotNull("Agenda for an empty room must not be null", agenda);
//         // The string may just be a header/date line — that is fine
//     }

//     // ─────────────────────────────────────────
//     // A-03 — Agenda: Person Agenda Has Meetings
//     // ─────────────────────────────────────────

//     /**
//      * A-03: A person with two booked meetings must have both descriptions
//      * appear in their printed agenda.
//      */
//     @Test
//     public void testPrintPersonAgenda_MultipleMeetings_AllAppear()
//             throws TimeConflictException {
//         Meeting m1 = new Meeting(3, 15, 9,  10, attendees, room, "Person Alpha");
//         Meeting m2 = new Meeting(3, 15, 11, 12, attendees, room, "Person Beta");
//         employee.addMeeting(m1);
//         employee.addMeeting(m2);

//         String agenda = employee.printAgenda(3, 15);
//         assertTrue("Alpha meeting should appear in person agenda", agenda.contains("Person Alpha"));
//         assertTrue("Beta meeting should appear in person agenda",  agenda.contains("Person Beta"));
//     }

//     // ─────────────────────────────────────────
//     // A-04 — Agenda: Null Agenda Request
//     // ─────────────────────────────────────────

//     /**
//      * A-04: Passing an out-of-range or null-equivalent date to printAgenda
//      * must throw an exception rather than return garbage output.
//      */
//     @Test(expected = Exception.class)
//     public void testPrintAgenda_InvalidMonth_ThrowsException() throws Exception {
//         // Month 0 is invalid — implementation should throw
//         room.printAgenda(0, 1);
//     }

//     @Test(expected = Exception.class)
//     public void testPrintAgenda_InvalidDay_ThrowsException() throws Exception {
//         // Day 0 is invalid — implementation should throw
//         room.printAgenda(3, 0);
//     }
// }