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

package tests;

import main.com.joebentley.mud.GameDatabaseConnection;
import main.com.joebentley.mud.ServerConnection;
import main.com.joebentley.mud.handlers.RegistrationHandler;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertTrue;

public class RegistrationHandlerTest {
    private GameDatabaseConnection connection;
    private RegistrationHandler registrationHandler;
    private ServerConnection serverConnection;
    private StringWriter writer;

    @Before
    public void setUp() {
        registrationHandler = new RegistrationHandler();

        connection = new GameDatabaseConnection();
        connection.deleteUsername("test");

        serverConnection = new ServerConnection(new Socket());
        writer = new StringWriter();
        serverConnection.setOutputWriter(new PrintWriter(writer));
    }

    @Test
    public void testRegistering() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        registrationHandler.parse(serverConnection, "");

        assertTrue(writer.toString().equals("Username: "));
        writer.getBuffer().setLength(0); // clear

        // TODO: Test for error messages
        registrationHandler.parse(serverConnection, "test");

        assertTrue(writer.toString().equals("Password: "));
        writer.getBuffer().setLength(0); // clear

        registrationHandler.parse(serverConnection, "password");

        // should be in loginhandler now TODO: test for that!
        assertTrue(writer.toString().equals("Successfully created user test\nUsername: "));
        assertTrue(connection.getUsers().containsUsername("test"));
    }
}
