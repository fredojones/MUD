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

import main.com.joebentley.mud.BehaviourDispatcher;

import java.util.HashMap;
import java.util.Map;

public class Room implements Saveable {
    private String ID;
    private String name;
    private Map<String, String> exits;
    private Map<String, String> properties;
    private BehaviourDispatcher behaviourDispatcher;

    public Room() {
        exits = new HashMap<>();
        properties = new HashMap<>();
        behaviourDispatcher = new BehaviourDispatcher();
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

    public Map<String, String> getExits() {
        return exits;
    }

    public void setExits(Map<String, String> exits) {
        this.exits = exits;
    }

    public boolean hasExits() {
        return !exits.isEmpty();
    }

    public BehaviourDispatcher getBehaviourDispatcher() {
        return behaviourDispatcher;
    }

    public String toShortString() {
        return ID + ": " + name;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("ID:   ").append(ID).append("\n")
           .append("name: ").append(name).append("\n");

        if (!getExits().isEmpty())
            res.append("exits:\n");

        getExits().forEach((exit, ID) ->
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

        public Builder setExits(Map<String, String> exits) {
            room.setExits(exits);
            return this;
        }

        public Room build() {
            return room;
        }
    }
}
