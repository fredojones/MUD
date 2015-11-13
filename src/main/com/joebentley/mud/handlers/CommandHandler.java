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

package main.com.joebentley.mud.handlers;

import main.com.joebentley.mud.GameDatabaseConnection;
import main.com.joebentley.mud.Server;
import main.com.joebentley.mud.ServerConnection;
import main.com.joebentley.mud.exceptions.IDExistsException;
import main.com.joebentley.mud.exceptions.NoIDException;
import main.com.joebentley.mud.saveables.Room;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandHandler implements InputHandler {
    private static final Logger log = Logger.getLogger(ServerConnection.class.getName());
    private Dispatcher dispatcher;

    public CommandHandler() {
        dispatcher = new Dispatcher();
    }

    @Override
    public void parse(ServerConnection connection, String input) {
        List<String> splitInput = Arrays.asList(input.split(" "));

        String command = splitInput.get(0);
        List<String> arguments;

        // Check whether there are any arguments, if not just use empty List
        if (splitInput.size() == 1) {
            arguments = new ArrayList<>();
        } else {
            // Get all arguments of command
            arguments = splitInput.subList(1, splitInput.size());
        }

        dispatcher.dispatch(connection, command, arguments);
        connection.getOutputWriter().print(">");
    }

    private class Dispatcher {
        HashMap<String, BiConsumer<ServerConnection, List<String>>> functions;

        public Dispatcher() {
            functions = new HashMap<>();

            functions.put("users", (serverConnection, s) ->
                    Server.game.getOnlineUsers().forEach(user ->
                            serverConnection.getOutputWriter().println(user.getUsername())
                    )
            );

            functions.put("quit", (serverConnection, strings) -> {
                try {
                    serverConnection.close();
                } catch (IOException e) {
                    log.log(Level.SEVERE, e.getMessage());
                }
            });

            functions.put("create", (serverConnection, strings) -> {
                if (strings.size() == 0) {
                    serverConnection.getOutputWriter().println("No arguments");
                    return;
                }

                switch (strings.get(0)) {
                    case "room": {
                        GameDatabaseConnection conn = serverConnection.getDatabaseConnection();
                        String ID = conn.getNextRoomID();

                        try {
                            conn.addRoomID(ID);
                        } catch (IDExistsException e) {
                            serverConnection.getOutputWriter().println("Couldn't add room (ID exists)");
                            log.log(Level.SEVERE, "Room save failed", e);
                            return;
                        }

                        serverConnection.getOutputWriter().println("Room created with ID " + ID);
                        break;
                    }
                }
            });

            functions.put("update", (serverConnection, strings) -> {
                if (strings.size() == 0) {
                    serverConnection.getOutputWriter().println("No arguments");
                    return;
                }

                switch (strings.get(0)) {
                    case "room": {
                        // TODO: Allow adding exits/properties here
                        // TODO: Allow multi word names

                        if (strings.size() < 4) {
                            serverConnection.getOutputWriter().println("Not enough arguments");
                            return;
                        }

                        String ID = strings.get(1);
                        // If no room with given ID exists yet in the database, build a new one with ID given
                        Room room = serverConnection.getDatabaseConnection().getRooms().getByID(ID)
                                .orElse(new Room.Builder().setID(ID).build());

                        switch (strings.get(2)) {
                            case "name":
                                // Join all strings after "name" with spaces
                                room.setName(strings.subList(3, strings.size()).stream()
                                                    .reduce("", (s, s2) -> s + " " + s2)
                                                    .trim());
                                break;
                        }

                        GameDatabaseConnection conn = serverConnection.getDatabaseConnection();

                        try {
                            conn.updateRoom(ID, room);
                        } catch (NoIDException e) {
                            serverConnection.getOutputWriter().println("Given ID does not exist!");
                            log.log(Level.SEVERE, "Error updating room", e);
                        }

                        break;
                    }
                }
            });

            // TODO: Function for viewing properties of object in DB
        }

        public void dispatch(ServerConnection connection, String command, List<String> arguments) {
            // ignore empty commands
            if (command.trim().length() == 0) {
                return;
            }

            functions.getOrDefault(command, (serverConnection, strings) ->
                    serverConnection.getOutputWriter().println("Unrecognized command")
            ).accept(connection, arguments);
        }
    }
}
