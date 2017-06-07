package model;

import model.message.ServerMessage;

/**
 * Created by Alexander on 07/06/2017.
 */
public interface ClientHandler {

    void addOutgoingMessage(ServerMessage message);

    String getName();

//    void setName(String name);

    int getSessionID();

    void start();

    void stop();

    void setUser(User user);

    User getUser();
}
