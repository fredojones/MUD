package tests;

import main.com.joebentley.mud.GameDatabaseConnection;
import main.com.joebentley.mud.User;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertTrue;

public class GameDatabaseConnectionTest {
    private static GameDatabaseConnection connection;
    private static User user;

    @BeforeClass
    public static void setUp() {
        connection = new GameDatabaseConnection();
        user = new User("test");
        user.getNewID(connection);
    }

    @Test
    public void savingAndGettingNewUser() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        connection.newUser(user, "password");

        assertTrue(connection.isUserSaved(user));
        assertTrue(connection.getUsers().containsUsername(user.getUsername()));
    }

    @Test
    public void savingUserMultipleTimes() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        connection.newUser(user, "password");
        user.setUsername("joe");
        connection.updateUserGivenByID(user.getID(), user);

        assertTrue(connection.isUserSaved(user));
        assertTrue(connection.getUsers().containsUsername(user.getUsername()));

        user.setUsername("test");
    }

    @Test
    public void verifyingPassword() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        connection.newUser(user, "password");

        assertTrue(connection.verifyPassword(user, "password"));
    }
}
