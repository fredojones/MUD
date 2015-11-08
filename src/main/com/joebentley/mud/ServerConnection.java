package main.com.joebentley.mud;

import main.com.joebentley.mud.handlers.InputHandler;
import main.com.joebentley.mud.handlers.LoginHandler;
import main.com.joebentley.mud.handlers.RegistrationHandler;

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

            // TODO: Move this somewhere else
            // Ask user whether they want to login or register
            outputWriter.print("register or login? ");
            outputWriter.flush();

            outer:
            while ((inputLine = in.readLine()) != null) {
                switch (inputLine.trim()) {
                    case "register":
                        this.inputHandler = new RegistrationHandler();
                        break outer;
                    case "login":
                        this.inputHandler = new LoginHandler();
                        break outer;
                    default:
                        outputWriter.println("Please enter \"register\" or \"login\"");
                        outputWriter.flush();
                }
            }

            // Force user prompt
            this.inputHandler.parse(this, "");
            outputWriter.flush();

            while (running && (inputLine = in.readLine()) != null) {
                inputHandler.parse(this, inputLine.trim());
                outputWriter.flush();
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }
}
