package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Alexander on 29/05/2017.
 */
public class Server {
    private static final short PORT = 5000;
    private static final int CAPACITY = 100;
//    private ServerSocket serverSocket;
    private List<String> participants = new ArrayList<>();
    private List<ClientHandler> clientHandlers = new ArrayList<>();
    private BlockingQueue<Message> buffer = new ArrayBlockingQueue<Message>(10);

    private static final Logger log = LogManager.getLogger(Server.class);

    public Server() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while(!Thread.interrupted()) {
                Socket socket = serverSocket.accept();
                log.info("new client accepted");
                clientHandlers.add(new ClientHandler(this, socket));
            }
        } catch (IOException e) {
            log.error("server socket creating error");
            System.exit(-1);
        }
        log.info("thread interrupted");
    }

    public BlockingQueue<Message> getBuffer() {
        return buffer;
    }

    public List<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }

    public List<String> getParticipants() {
        return participants;
    }

    private void go() {

    }

    public static void main(String[] args) {
        Thread serverThread = new Thread(() -> {
            new Server();
        }, "Server");
        serverThread.start();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("to exit enter exit");
            while (br.readLine().compareToIgnoreCase("exit") != 0) {
                System.out.println("here");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverThread.interrupt();


    }
}
