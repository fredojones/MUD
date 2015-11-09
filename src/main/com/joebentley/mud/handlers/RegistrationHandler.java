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

import main.com.joebentley.mud.ServerConnection;
import main.com.joebentley.mud.User;
import main.com.joebentley.mud.exceptions.UsernameAlreadyExistsException;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrationHandler implements InputHandler {
    private static final Logger log = Logger.getLogger(ServerConnection.class.getName());

    private String username;
    private RegisterState state = RegisterState.NO;

    @Override
    public void parse(ServerConnection connection, String input) {
        // The user has just started registering, prompt for username
        if (state == RegisterState.NO) {
            // TODO: Maybe make these strings external i.e. kept in some resources class
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
                log.log(Level.SEVERE, "Error creating user", e);
                connection.getOutputWriter().println("Error creating new user");
                state = RegisterState.NO;
                return;
            } catch (UsernameAlreadyExistsException e) {
                log.log(Level.SEVERE, "Error username already exists in redis", e);
                connection.getOutputWriter().println("Error username already exists");
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
