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

package main.com.joebentley.mud;

import main.com.joebentley.mud.exceptions.UsernameAlreadyExistsException;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class GameDatabaseConnection extends DatabaseConnection {

    /**
     * Get next user ID from the database
     *
     * @return next user ID
     */
    public synchronized String getNextUserID() {
        // id 1 is reserved for test user
        String ID = getOrElse("user:nextid", "2");
        connection.incr("user:nextid");
        return ID;
    }

    /**
     * Save new user to the database
     *
     * @param user to add to database
     */
    public void newUser(User user, String password)
            throws NoSuchAlgorithmException, UnsupportedEncodingException, UsernameAlreadyExistsException {

        connection.sadd("user:ids", user.getID());

        if (getUsers().containsUsername(user.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }

        // Generate salt
        SecureRandom random = new SecureRandom();
        String salt = new BigInteger(130, random).toString(32);

        // Generate hash
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update((password + salt).getBytes("UTF-8"));

        String digest = String.format("%064x", new java.math.BigInteger(1, md.digest()));

        Map<String, String> hash = new HashMap<>();
        hash.put("username", user.getUsername());
        hash.put("hashed", digest);
        hash.put("salt", salt);

        connection.hmset("user:" + user.getID(), hash);
    }

    /**
     * Set user with ID to newUser, doesn't change password
     *
     * @param ID      ID to change
     * @param newUser user to set ID to
     */
    public void updateUserGivenByID(String ID, User newUser) {
        connection.hset("user:" + ID, "username", newUser.getUsername());
    }

    /**
     * Test whether user has password password
     *
     * @param user     user to test for
     * @param password password to test that the user has
     * @return true if correct password
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public boolean verifyPassword(User user, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        Map<String, String> hash = connection.hgetAll("user:" + user.getID());

        if (hash.isEmpty()) {
            return false;   // TODO: Maybe should throw exception?
        }

        String expected = hash.get("hashed");
        String salt = hash.get("salt");

        // generate hash
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update((password + salt).getBytes("UTF-8"));

        String digest = String.format("%064x", new java.math.BigInteger(1, md.digest()));

        return digest.equals(expected);
    }

    /**
     * Get all users from the database
     *
     * @return all saved users
     */
    public Users getUsers() {
        Users users = new Users();

        connection.sinter("user:ids").forEach(ID -> {
                    if (connection.exists("user:" + ID)) {
                        User user = new User();
                        user.setID(ID);
                        user.setUsername(connection.hget("user:" + ID, "username"));
                        users.add(user);
                    }
                }
        );

        return users;
    }

    /**
     * Check whether given user is saved in the database
     *
     * @param user to test if saved
     * @return true if saved
     */
    public boolean isUserSaved(User user) {
        return connection.exists("user:" + user.getID());
    }

    /**
     * Delete user from database
     *
     * @param user to delete
     */
    public void deleteUser(User user) {
        connection.del("user:" + user.getID());
        connection.srem("user:ids", user.getID());
    }

    /**
     * Delete user with username
     *
     * @param username username to delete
     */
    public void deleteUsername(String username) {
        getUsers().forEach(user -> {
            if (user.getUsername().equals(username)) {
                deleteUser(user);
            }
        });
    }
}
