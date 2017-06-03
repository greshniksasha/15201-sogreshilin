package model;

import model.message.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by Alexander on 03/06/2017.
 */
public class ClientMessageHandler implements MessageHandler {

    private Client client;
    private static final Logger log = LogManager.getLogger(Client.class);

    public ClientMessageHandler(Client client) {
        this.client = client;
    }

    @Override
    public void process(LoginResponse message) {
        client.notifyObservers(message);
        if (message.succeeded()) {
            client.setSessionID(message.getSessionID());
            client.loggedIn(true);
            log.info("logged in as {}", client.getName());
            log.info("session ID is {}", client.getSessionID());
            client.addOutgoingMessage(new ListUsersRequest(message.getSessionID()));
        } else {
            log.info(message.getError());
        }
    }

    @Override
    public void process(LastMessages message) {
        client.notifyObservers(message);
        log.info("got lastMessages : {}", message.getMessages().toString());
    }

    @Override
    public void process(ListUsersResponse message) {
        if (message.succeeded()) {
            for (String user : message.getUsers()) {
                client.addUser(user);
                log.info("add user to userlist {}", user);
            }
            client.notifyObservers(message);
        } else {
            log.info(message.getError());
        }
    }

    @Override
    public void process(LogoutResponse message) {
        if (message.succeeded()) {
            client.finish();
            client.loggedIn(false);
            log.info("logout succeeded");
        } else {
            log.info("logout failed");
        }
        client.notifyObservers(message);
    }

    @Override
    public void process(TextResponse message) {
        if (message.succeeded()) {
            log.info("message delivered");
        } else {
            log.info("message delivering failed");
        }
    }

    @Override
    public void process(UserLoginMessage message) {
        if (client.loggedIn()) {
            client.addUser(message.getName());
            client.notifyObservers(message);
        }
    }

    @Override
    public void process(UserLogoutMessage message) {
        if (client.loggedIn()) {
            client.removeUser(message.getName());
            client.notifyObservers(message);
        }
    }

    @Override
    public void process(UserMessage message) {
        log.info("got message \"{}\" from {}", message.getMessage(), message.getName());
        client.notifyObservers(message);
    }
}
