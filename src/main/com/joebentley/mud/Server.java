package main.com.joebentley.mud;

import main.com.joebentley.mud.handlers.LoginHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable {
    public static final int PORT_NUMBER = 1024;
    public static final Game game = new Game();
    private static final Logger log = Logger.getLogger(Server.class.getName());
    private ExecutorService executor;
    private boolean listening = true;

    public Server() {
        executor = Executors.newCachedThreadPool();
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(PORT_NUMBER)) {
            log.log(Level.INFO, "Listening on " + PORT_NUMBER + "\n");

            while (listening) {
                Socket clientSocket = serverSocket.accept();

                log.info("New connection " + clientSocket.getInetAddress());

                ServerConnection connection = new ServerConnection(clientSocket, new LoginHandler());

                FutureTask<?> f = new FutureTask<>(connection, null);
                executor.execute(f);
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }
}


