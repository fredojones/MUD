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

import main.com.joebentley.mud.saveables.Room;
import main.com.joebentley.mud.saveables.Rooms;
import main.com.joebentley.mud.saveables.User;
import main.com.joebentley.mud.saveables.Users;

public class Game {
    private Users onlineUsers;
    private Rooms rooms;

    public Game() {
        onlineUsers = new Users();
        rooms = new Rooms();
    }

    /**
     * Get list of users that are currently logged in
     *
     * @return users logged in
     */
    public Users getOnlineUsers() {
        return onlineUsers;
    }

    /**
     * Add user to list of online users
     *
     * @param user user to add
     */
    public void addOnlineUser(User user) {
        onlineUsers.put(user.getID(), user);
    }

    /**
     * Get list of current rooms in game
     *
     * @return rooms in game
     */
    public Rooms getRooms() {
        return rooms;
    }

    /**
     * Update room at ID with room
     *
     * @param ID to update
     * @param room to update with
     */
    public void updateRoom(String ID, Room room) {
        rooms.put(ID, room);
    }
}
