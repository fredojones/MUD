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

package main.com.joebentley.mud;

import main.com.joebentley.mud.handlers.InputHandler;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerConnection implements Runnable, Closeable {
    private static final Logger log = Logger.getLogger(ServerConnection.class.getName());

    private Socket clientSocket;
    private InputHandler inputHandler;
    private PrintWriter outputWriter;
    private GameDatabaseConnection databaseConnection;
    private User user;  // User logged into by this ServerConnection

    private boolean running = true;

    public ServerConnection(Socket clientSocket) {
        this.clientSocket = clientSocket;

        try {
            this.outputWriter = new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException e) {
            this.outputWriter = null;
        }

        databaseConnection = new GameDatabaseConnection();
    }

    public ServerConnection(Socket clientSocket, InputHandler inputHandler) {
        this(clientSocket);
        this.inputHandler = inputHandler;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public PrintWriter getOutputWriter() {
        return outputWriter;
    }

    public void setOutputWriter(PrintWriter outputWriter) {
        this.outputWriter = outputWriter;
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }

    public void setHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    public void runHandler(String command) {
        inputHandler.parse(this, command);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GameDatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }

    @Override
    public void close() throws IOException {
        running = false;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String inputLine;

            // Force user prompt
            runHandler("");
            outputWriter.flush();

            while (running && (inputLine = in.readLine()) != null) {
                runHandler(inputLine.trim());
                outputWriter.flush();
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Connection Error", e);
        } finally {
            // Always remove user when logging out
            Server.game.getOnlineUsers().remove(user);
        }
    }
}
