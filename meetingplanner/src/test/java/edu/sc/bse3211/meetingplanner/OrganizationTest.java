package edu.sc.bse3211.meetingplanner;

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

}
