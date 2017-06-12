package model;

import model.message.ServerMessage;
import model.message.UserLogoutMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Alexander on 07/06/2017.
 */
public abstract class ClientHandler {
    protected Thread reader;
    protected Thread writer;
    protected int sessionID;
    protected Server server;
    protected Socket socket;
    protected BlockingQueue<ServerMessage> messagesToSend;
    protected User user = new User();
    protected static AtomicInteger sessionIDGenerator = new AtomicInteger(0);
    protected static final int QUEUE_CAPACITY = 1000;
    protected static final Logger log = LogManager.getLogger(ClientHandler.class);

    protected void blockUser() throws IOException {
        if (!server.getUsers().isEmpty()) {
            server.getUsers().remove(user);
        }
        UserLogoutMessage msg = new UserLogoutMessage();
        msg.setName(getName());
        server.getServerMessages().add(msg);
        new Thread(() -> {server.removeClientHandler(this);}).start();
        writer.interrupt();
        closeSocket();
    }

    public void addOutgoingMessage(ServerMessage message) {
        messagesToSend.add(message);
    }

    public String getName() {
        return user.getName();
    }

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

    public void interruptWriter() {
        writer.interrupt();
    }

    public void closeSocket() throws IOException {
        socket.close();
    }

    public void setUser(User user) {
        this.user = user;
    }
}
