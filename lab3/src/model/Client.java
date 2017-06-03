package model;

import model.message.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.ClientForm;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Alexander on 29/05/2017.
 */
public class Client {
    private static final String IP = "localhost";
    private static final short PORT = 5000;
    private static final String CHAT_CLIENT_NAME = "My_Client";
    private Socket socket;

    private String name;
    private int sessionID;
    private Boolean loggedIn;
    private BlockingQueue<ClientMessage> queue;
    private List<String> users;
    private List<IncomingMsgObserver> observers;

    private MessageHandler messageHandler;
    private ObjectInputStream readerStream;
    private ObjectOutputStream writerStream;
    private Thread readerThread;
    private Thread writerThread;

    private static final int CAPACITY = 100;
    private static final Logger log = LogManager.getLogger(Client.class);

    public Client() {
        queue = new ArrayBlockingQueue<>(CAPACITY);
        users = new ArrayList<>();
        observers = new ArrayList<>();
        messageHandler = new ClientMessageHandler(this);
        loggedIn = false;
    }

    private void connectToServer() {
        try {
            socket = new Socket(IP, PORT);
        } catch (IOException e) {
            log.error("connecting to server error");
        }
        log.info("connected to server");
    }

    public Boolean loggedIn() {
        return loggedIn;
    }

    public void loggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    private void go () {
        try {
            readerThread = new Thread(new Reader(), "Reader");
            writerThread = new Thread(new Writer(), "Writer");

            readerThread.start();
            writerThread.start();
            readerThread.join();
            writerThread.join();

        } catch (InterruptedException e) {
            log.info("interrupted");
        }
    }


    public List<String> getUsers() {
        return users;
    }

    public void addOutgoingMessage(ClientMessage message) {
        queue.add(message);
        log.info("message added to the queue");
    }

    public static String getChatClientName() {
        return CHAT_CLIENT_NAME;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void addUser(String name) {
        if (!users.contains(name)) {
            users.add(name);
        }
    }

    public void addObserver(IncomingMsgObserver o) {
        observers.add(o);
    }

//    public void removeObserver(IncomingMsgObserver o) {
//        observers.remove(o);
//    }

    public void notifyObservers(ServerMessage message) {
        for (IncomingMsgObserver o : observers) {
            o.process(message);
        }
    }

    public interface IncomingMsgObserver {
        void process(ServerMessage message);
    }

    public void removeUser(String name) {
        users.remove(name);
    }

    public void finish() {
        readerThread.interrupt();
        writerThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            log.error("closing socket error");
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.connectToServer();
        new ClientForm(client).setVisible(true);
        client.go();
    }

    private class Writer implements Runnable {
        @Override
        public void run() {
            try {
                ObjectOutputStream writerStream = new ObjectOutputStream(socket.getOutputStream());
                while(!Thread.interrupted()) {
                    ClientMessage message = queue.take();
                    writerStream.writeObject(message);
                    writerStream.flush();
                    log.info("client message written to server");
                }
            } catch (InterruptedException e) {
                log.info("interrupted");
            } catch (IOException e) {
                log.info("could not write message");
            }
        }
    }

    private class Reader implements Runnable {
        @Override
        public void run() {
            try {
                ObjectInputStream readerStream = new ObjectInputStream(socket.getInputStream());
                while(!Thread.interrupted()) {
                    ServerMessage message = (ServerMessage)readerStream.readObject();
                    message.process(messageHandler);
                }
            } catch (IOException | ClassNotFoundException e) {
                log.info("socket closed");
            } finally {
                log.info("finished");
            }
        }
    }



}
