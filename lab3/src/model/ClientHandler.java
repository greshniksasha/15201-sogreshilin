package model;

import model.message.ServerMessage;

import java.io.IOException;

/**
 * Created by Alexander on 07/06/2017.
 */
public interface ClientHandler {

    void addOutgoingMessage(ServerMessage message);

    String getName();

    int getSessionID();

    void start();

    void interruptWriter();

    void closeSocket() throws IOException;

    void setUser(User user);

    User getUser();
}
