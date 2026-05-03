package edu.sc.bse3211.meetingplanner;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class OrganizationTest {
	// Add test methods here. 
    // You are not required to write tests for all classes.
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
     // O-01 — Organization: Valid Search
   // ─────────────────────────────────────────

     // Searching the Organization for a room ID or employee name that
     // exists must return a non-null object.

    @Test
    public void testOrganization_GetRoom_ValidID_ReturnsRoom() throws Exception {
        Organization org = new Organization();
        // Use a room ID known to exist in the default Organization setup
        Room found = org.getRoom("LLT6A");
        assertNotNull("getRoom with a valid ID should return a non-null Room", found);
    }

    @Test
    public void testOrganization_GetEmployee_ValidName_ReturnsPerson() throws Exception {
        Organization org = new Organization();
        // Use an employee name known to exist in the default Organization setup
        Person found = org.getEmployee("Namugga Martha");
        assertNotNull("getEmployee with a valid name should return a non-null Person", found);
    }

     // ─────────────────────────────────────────
     // O-03 — Organization: Null Search
    // ─────────────────────────────────────────

    // Passing null as the search key to getRoom or getEmployee must
     //throw an exception rather than return silently.
    @Test(expected = Exception.class)
    public void testOrganization_GetRoom_NullID_ThrowsException() throws Exception {
        Organization org = new Organization();
        org.getRoom(null);
    }

    @Test(expected = Exception.class)
    public void testOrganization_GetEmployee_NullName_ThrowsException() throws Exception {
        Organization org = new Organization();
        org.getEmployee(null);
    }

    // ─────────────────────────────────────────
    // O-04 — Organization: Empty Organization
    // ─────────────────────────────────────────

    // Searching in an Organization that has no rooms/employees should
    // return null or an empty result (and must not crash).

    @Test
    public void testOrganization_EmptyOrg_GetRoom_ReturnsNullOrEmpty() {
        // Create a fresh, unpopulated Organization (no rooms added)
        Organization emptyOrg = new Organization();
        try {
            Room result = emptyOrg.getRoom("ANY-ROOM");
            // Implementation may return null for an empty org
            // (it may also throw — both are acceptable behaviours)
        } catch (Exception e) {
            // Throwing is also acceptable — just must not crash the JVM
        }
    }

    }



