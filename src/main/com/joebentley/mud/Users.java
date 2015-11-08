package main.com.joebentley.mud;

import java.util.ArrayList;

public class Users extends ArrayList<User> {

    /**
     * Check if users list contains user with usernam
     *
     * @param username to test for
     * @return true if username exists
     */
    public boolean containsUsername(String username) {
        return this.stream().filter(user -> user.getUsername().equals(username)).findAny().isPresent();
    }

    /**
     * Get first user with username
     *
     * @param username to look for
     * @return user with username
     */
    public User getByUsername(String username) {
        return this.stream().filter(user -> user.getUsername().equals(username)).findAny().orElse(null);
    }
}
