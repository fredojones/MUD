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

import java.util.HashMap;
import java.util.Map;

public class Room implements Saveable {
    private String ID;
    private String name;
    private Map<Exit, String> exits;

    public enum Exit {
        NORTH, SOUTH, EAST, WEST
    }

    public Room() {
        exits = new HashMap<>();
    }

    @Override
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Map<Exit, String> getExits() {
        return exits;
    }

    public void setExits(Map<Exit, String> exits) {
        this.exits = exits;
    }

    public Map<String, String> getStringExits() {
        Map<String, String> exitStrings = new HashMap<>();
        exits.forEach((exit, ID) -> exitStrings.put(exit.toString(), ID));
        return exitStrings;
    }

    // TODO: Write test
    public void setStringExits(Map<String, String> stringExits) {
        stringExits.forEach((exit, ID) -> exits.put(Exit.valueOf(exit), ID));
    }

    public static class Builder {
        Room room;

        public Builder() {
            room = new Room();
        }

        public Builder setID(String ID) {
            room.setID(ID);
            return this;
        }

        public Builder setExits(Map<Exit, String> exits) {
            room.setExits(exits);
            return this;
        }

        public Builder setStringExits(Map<String, String> exits) {
            room.setStringExits(exits);
            return this;
        }

        public Room build() {
            return room;
        }
    }
}
