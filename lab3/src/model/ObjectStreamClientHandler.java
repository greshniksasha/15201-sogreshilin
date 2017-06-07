package model;

import model.message.ClientMessage;
import model.message.ServerMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Alexander on 07/06/2017.
 */
public class ObjectStreamClientHandler implements ClientHandler {
    private Thread reader;
    private Thread writer;
    private int sessionID;
//    private String name;
    private Socket socket;
    private BlockingQueue<ServerMessage> messagesToSend;
    private User user;

    private static final Logger log = LogManager.getLogger(ObjectStreamClientHandler.class);
    private static AtomicInteger sessionIDGenerator = new AtomicInteger(0);
    private static final int QUEUE_CAPACITY = 1000;

    ObjectStreamClientHandler(Server server, Socket socket) {
        this.socket = socket;
        sessionID = sessionIDGenerator.getAndIncrement();
        messagesToSend = new ArrayBlockingQueue<ServerMessage>(QUEUE_CAPACITY);

        reader = new Thread(() -> {
            try {
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                log.info("got input stream file descriptor");
                while (!Thread.interrupted()) {
                    ClientMessage m = (ClientMessage)inputStream.readObject();
                    log.info("объект прочитан");
                    m.process(server, this);
                }
            } catch (IOException e) {
                log.info("socket has been closed");
            } catch (ClassNotFoundException e) {
                log.info("class not found");
            }
        }, "ObjectReader-" + sessionID);

        writer = new Thread(() -> {
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                log.info("got output stream file descriptor");
                while (!Thread.interrupted()) {
                    ServerMessage m = messagesToSend.take();
                    outputStream.writeObject(m);
                    outputStream.flush();
                    log.info("message writen to {}", getName());
                }
            } catch (IOException e) {
                log.error("could not get output stream");
            } catch (InterruptedException e) {
                log.info("interrupted");
            }
        }, "ObjectWriter-" + sessionID);

    }

    public void addOutgoingMessage(ServerMessage message) {
        messagesToSend.add(message);
    }

    public String getName() {
        return user.getName();
    }

//    public void setName(String name) {
//        this.name = name;
//    }

    @Override
    public User getUser() {
        return user;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void start() {
        reader.start();
        writer.start();
    }

    public void stop() {
        writer.interrupt();
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
}

