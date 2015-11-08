package main.com.joebentley.mud.handlers;

import main.com.joebentley.mud.ServerConnection;
import main.com.joebentley.mud.User;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class RegistrationHandler implements InputHandler {
    private String username;
    private RegisterState state = RegisterState.NO;

    @Override
    public void parse(ServerConnection connection, String input) {
        // The user has just started registering, prompt for username
        if (state == RegisterState.NO) {
            connection.getOutputWriter().print("Username: ");
            state = RegisterState.USERNAME;
            return;
        }

        // The user has just entered a username, check it exists, then prompt for password
        if (state == RegisterState.USERNAME) {
            if (input.length() == 0) {
                connection.getOutputWriter().println("No username entered!");
                connection.getOutputWriter().print("Username: ");
                state = RegisterState.USERNAME;
                return;
            }

            // Check if username already exists
            if (connection.getDatabaseConnection().getUsers().containsUsername(input)) {
                connection.getOutputWriter().println("Username already exists");
                state = RegisterState.NO;
                return;
            }

            username = input;
            connection.getOutputWriter().print("Password: ");
            state = RegisterState.PASSWORD;
            return;
        }

        // The user has just entered a password, add new user
        if (state == RegisterState.PASSWORD) {
            if (input.length() == 0) {
                connection.getOutputWriter().println("No password entered!");
                connection.getOutputWriter().print("Password: ");
                state = RegisterState.PASSWORD;
                return;
            }

            User user = new User.Builder(connection.getDatabaseConnection()).setUsername(username).build();

            try {
                connection.getDatabaseConnection().newUser(user, input);
            } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
                e.printStackTrace();
                connection.getOutputWriter().println("Error creating new user");
                state = RegisterState.NO;
                return;
            }

            connection.getOutputWriter().println("Successfully created user " + username);

            // force user to re-login
            connection.setHandler(new LoginHandler());
            connection.runHandler("login");
        }
    }

    private enum RegisterState {
        NO, USERNAME, PASSWORD
    }
}
