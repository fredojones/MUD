package main.com.joebentley.mud.handlers;

import main.com.joebentley.mud.Server;
import main.com.joebentley.mud.ServerConnection;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
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
        String[] splitInput = input.split(" ");

        String command = splitInput[0];
        String[] arguments = Arrays.copyOfRange(splitInput, 1, splitInput.length);

        dispatcher.dispatch(connection, command, arguments);
        connection.getOutputWriter().print(">");
    }

    private class Dispatcher {
        HashMap<String, BiConsumer<ServerConnection, String[]>> functions;

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
        }

        public void dispatch(ServerConnection connection, String command, String[] arguments) {
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
