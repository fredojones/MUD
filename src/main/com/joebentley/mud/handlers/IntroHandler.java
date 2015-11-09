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

public class IntroHandler implements InputHandler {
    private IntroState introState = IntroState.PROMPT;

    private enum IntroState {
        PROMPT, INPUT
    }

    @Override
    public void parse(ServerConnection connection, String input) {
        // Ask user whether they want to login or register
        if (introState == IntroState.PROMPT && input.isEmpty()) {
            connection.getOutputWriter().print("register or login? ");
            connection.getOutputWriter().flush();
            introState = IntroState.INPUT;
            return;
        }

        // Get the input
        switch (input) {
            case "register":
                connection.setHandler(new RegistrationHandler());
                // Force new prompt
                connection.runHandler("");
                break;
            case "login":
                connection.setHandler(new LoginHandler());
                // Force new prompt
                connection.runHandler("");
                break;
            default:
                connection.getOutputWriter().println("Please enter \"register\" or \"login\"");
                connection.getOutputWriter().flush();
        }
    }
}
