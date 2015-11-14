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
    private Map<String, String> properties;

    public enum Exit {
        NORTH, SOUTH, EAST, WEST
    }

    public Room() {
        exits = new HashMap<>();
        properties = new HashMap<>();
    }

    @Override
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }

    public Map<Exit, String> getExits() {
        return exits;
    }

    public void setExits(Map<Exit, String> exits) {
        this.exits = exits;
    }

    // TODO: Write test
    public Map<String, String> getStringExits() {
        Map<String, String> exitStrings = new HashMap<>();
        exits.forEach((exit, ID) -> exitStrings.put(exit.toString(), ID));
        return exitStrings;
    }

    public boolean hasExits() {
        return !exits.isEmpty();
    }

    // TODO: Write test
    public void setStringExits(Map<String, String> stringExits) {
        stringExits.forEach((exit, ID) -> exits.put(Exit.valueOf(exit), ID));
    }

    public String toShortString() {
        return ID + ": " + name;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("ID:   ").append(ID).append("\n")
           .append("name: ").append(name).append("\n");

        if (!getStringExits().isEmpty())
            res.append("exits:\n");

        getStringExits().forEach((exit, ID) ->
            res.append(exit).append(" -> ").append(ID).append("\n")
        );

        return res.toString();
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

        public Builder setName(String name) {
            room.setName(name);
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
