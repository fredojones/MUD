package main.com.joebentley.mud.handlers;

import main.com.joebentley.mud.Server;
import main.com.joebentley.mud.ServerConnection;
import main.com.joebentley.mud.User;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;


public class LoginHandler implements InputHandler {
    private LoginState state = LoginState.NO;
    private String username;

    @Override
    public void parse(ServerConnection connection, String input) {
        // The user has just started logging in, prompt for username
        if (state == LoginState.NO) {
            state = LoginState.USERNAME;
            connection.getOutputWriter().print("Username: ");
            return;
        }

        // The user has given a username, prompt for password
        if (state == LoginState.USERNAME) {
            if (input.length() == 0) {
                // Restart login process
                state = LoginState.NO;
                connection.getOutputWriter().println("No username specified");
                parse(connection, "");
                return;
            }

            username = input;
            state = LoginState.PASSWORD;
            connection.getOutputWriter().print("Password: ");
            return;
        }

        // The user has given a password, log them in
        if (state == LoginState.PASSWORD) {
            if (input.length() == 0) {
                // Restart login process
                state = LoginState.NO;
                connection.getOutputWriter().println("No password specified");
                parse(connection, "");
                return;
            }

            User user = connection.getDatabaseConnection().getUsers().getByUsername(username);

            // Does the user exist?
            if (user != null) {
                boolean correct;

                try {
                    correct = connection.getDatabaseConnection().verifyPassword(user, input);
                } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    connection.getOutputWriter().println("Error verifying password");
                    state = LoginState.NO;
                    return;
                }

                if (correct) {
                    // Set the user as online
                    Server.game.addOnlineUser(user);
                    connection.setUser(user);
                    connection.getOutputWriter().println("Logged in as " + username + "! Welcome.");

                    // Go to idling handler
                    connection.setHandler(new CommandHandler());
                    connection.runHandler("");
                    return;
                }
            }

            // If we got here, something went wrong, so restart process
            state = LoginState.NO;
            connection.getOutputWriter().println("Invalid username or password");
            parse(connection, "login");
        }
    }

    // Current state of login state machine
    private enum LoginState {
        NO, USERNAME, PASSWORD
    }
}
