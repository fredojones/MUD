package main.com.joebentley.mud.handlers;

import main.com.joebentley.mud.ServerConnection;

public interface InputHandler {
    void parse(ServerConnection connection, String input);
}
