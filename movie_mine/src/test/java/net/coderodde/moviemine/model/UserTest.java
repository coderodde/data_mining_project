package net.coderodde.moviemine.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {
    
    private final User user;
    
    public UserTest() {
        user = User.createUser()
                    .withId("1")
                    .asFemale()
                    .withAge(22)
                    .withOccupation("Nurse")
                    .withZipCode("00520");
    }

    @Test
    public void testGetId() {
        assertEquals("1", user.getId());
        assertFalse("-1".equals(user.getId()));
        assertFalse("0".equals(user.getId()));
        assertFalse("2".equals(user.getId()));
        assertFalse("3".equals(user.getId()));
    }

    @Test
    public void testGetGender() {
        assertEquals(User.Gender.FEMALE, user.getGender());
        assertFalse(User.Gender.MALE == user.getGender());
    }

    @Test
    public void testGetAge() {
        assertEquals(22, user.getAge());
        assertTrue(user.getAge() != 20);
        assertTrue(user.getAge() != 21);
        assertTrue(user.getAge() != 23);
        assertTrue(user.getAge() != 24);
    }

    @Test
    public void testGetOccupation() {
        assertEquals("Nurse", user.getOccupation());
        assertFalse(user.getOccupation().equals("nurse"));
        assertFalse(user.getOccupation().equals("uNrse"));
    }

    @Test
    public void testGetZipCode() {
        assertEquals("00520", user.getZipCode());
        assertFalse(user.getZipCode().equals("0520"));
        assertFalse(user.getZipCode().equals("0052"));
        assertFalse(user.getZipCode().equals("00510"));
    }
}
