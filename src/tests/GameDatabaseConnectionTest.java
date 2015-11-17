/*
 * Copyright (c) 2015
 *
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 *
 */

package tests;

import main.com.joebentley.mud.GameDatabaseConnection;
import main.com.joebentley.mud.exceptions.IDExistsException;
import main.com.joebentley.mud.exceptions.NoIDException;
import main.com.joebentley.mud.exceptions.UsernameAlreadyExistsException;
import main.com.joebentley.mud.saveables.Room;
import main.com.joebentley.mud.saveables.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class GameDatabaseConnectionTest {
    private static GameDatabaseConnection connection;
    private static User user;
    private static Room room;

    @BeforeClass
    public static void setUpClass() {
        connection = new GameDatabaseConnection();
        user = new User("test");
        user.getNewID(connection);

        Map<String, String> exits = new HashMap<>();
        exits.put("north", "2");
        exits.put("south", "5");
        room = new Room.Builder().setID(connection.getNextRoomID()).setExits(exits).setName("testroom").build();
    }

    @Before
    public void setUp() {
        // Remove all current test users
        connection.deleteUsername(user.getUsername());

        // Re-remove room
        connection.removeID(room);
    }

    @Test
    public void savingAndGettingNewUser() throws NoSuchAlgorithmException, UnsupportedEncodingException, UsernameAlreadyExistsException {
        connection.newUser(user, "password");

        assertTrue(connection.isUserSaved(user));
        assertTrue(connection.getUsers().containsUsername(user.getUsername()));
    }

    @Test
    public void savingAndUpdatingUser() throws NoSuchAlgorithmException, UnsupportedEncodingException, UsernameAlreadyExistsException {
        connection.newUser(user, "password");
        user.setUsername("joe");
        connection.updateUserGivenByID(user.getID(), user);

        assertTrue(connection.isUserSaved(user));
        assertTrue(connection.isIDRegistered(user));
        assertTrue(connection.getUsers().containsUsername(user.getUsername()));

        user.setUsername("test");
    }

    @Test(expected = UsernameAlreadyExistsException.class)
    public void savingUserMultipleTimesThrowsException() throws NoSuchAlgorithmException, UnsupportedEncodingException, UsernameAlreadyExistsException {
        connection.newUser(user, "password");
        connection.newUser(user, "password");
    }

    @Test
    public void savingUserThenDeletingThenSaving() throws NoSuchAlgorithmException, UnsupportedEncodingException, UsernameAlreadyExistsException {
        connection.newUser(user, "password");
        connection.deleteUser(user);
        connection.newUser(user, "password");
    }

    @Test
    public void savingUsernameThenDeletingThenSaving() throws NoSuchAlgorithmException, UnsupportedEncodingException, UsernameAlreadyExistsException {
        connection.newUser(user, "password");
        User user2 = new User.Builder(connection).setUsername(user.getUsername()).build();
        connection.deleteUsername(user.getUsername());
        connection.newUser(user2, "password");
    }

    @Test
    public void verifyingPassword() throws UnsupportedEncodingException, NoSuchAlgorithmException, UsernameAlreadyExistsException {
        connection.newUser(user, "password");

        assertTrue(connection.verifyPassword(user, "password"));
    }

    @Test
    public void addedRoomIDExists() throws IDExistsException {
        connection.addRoomID(room.getID());
        assertTrue(connection.isIDRegistered(room));
    }

    @Test
    public void addedRoomHasStoredProperly() throws IDExistsException, NoIDException {
        connection.addRoom(room);
        Room stored = connection.getRooms().get(room.getID());
        assertTrue(stored.getName().equals(room.getName()));
        assertTrue(stored.getExits().equals(room.getExits()));
    }

    @Test(expected = NoIDException.class)
    public void updatingNotAddedRoomThrowsException() throws NoIDException {
        connection.updateRoom(room.getID(), room);
    }
}
