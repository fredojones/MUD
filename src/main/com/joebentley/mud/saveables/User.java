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

package main.com.joebentley.mud.saveables;

import main.com.joebentley.mud.GameDatabaseConnection;

public class User implements Saveable {
    public static final User testUser = new User("test");

    static {
        testUser.setID("1");
        testUser.group = Group.OWNER;
    }

    public Group group;
    private String ID;
    private String username;

    public User(String username) {
        this.username = username;

        group = Group.PUBLIC;
    }

    public User() {
        this(null);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void getNewID(GameDatabaseConnection connection) {
        ID = connection.getNextUserID();
    }

    @Override
    public String getSaveableName() {
        return "user";
    }

    /**
     * Class to build a new User instance (handles getting new user ID, etc.
     */
    public static class Builder {
        private User user;

        public Builder(GameDatabaseConnection connection) {
            user = new User();
            user.getNewID(connection);
        }

        public Builder setUsername(String username) {
            user.setUsername(username);
            return this;
        }

        public User build() {
            return user;
        }
    }
}
