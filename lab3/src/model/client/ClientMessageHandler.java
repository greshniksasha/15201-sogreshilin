package model.client;

import model.User;
import model.message.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Alexander on 03/06/2017.
 */
public class ClientMessageHandler implements MessageHandler {

    private Client client;
    private static final Logger log = LogManager.getLogger(ClientMessageHandler.class);

    public ClientMessageHandler(Client client) {
        this.client = client;
    }

    @Override
    public void process(LoginSuccess message) {
        client.notifyObservers(message);
        client.setSessionID(message.getSessionID());
        client.loggedIn(true);
        log.info("processing " + message.getClass());
        ListUsersRequest msg = new ListUsersRequest();
        msg.setSessionID(message.getSessionID());
        client.addOutgoingMessage(msg);
    }

    @Override
    public void process(LoginError message) {
        client.notifyObservers(message);
        log.info("processing " + message.getClass());
    }

    @Override
    public void process(ListUsersSuccess message) {
        for (User user : message.getUsers()) {
            client.addUser(user);
            log.info("processing " + message.getClass());
        }
        client.notifyObservers(message);
    }

    @Override
    public void process(ListUsersError message) {
        log.info("processing " + message.getClass());
    }

    @Override
    public void process(TextSuccess message) {
        client.notifyObservers(message);
        log.info("processing " + message.getClass());
    }

    @Override
    public void process(TextError message) {
        TextMessage undeliveredMessage = client.takeTextMessage();
        message.setText(undeliveredMessage.getText());
        client.notifyObservers(message);
        log.info("processing " + message.getClass());
    }

    @Override
    public void process(LogoutSuccess message) {
        client.removeAllUsers();
        client.notifyObservers(message);
        client.finish();
        client.loggedIn(false);
        log.info("processing " + message.getClass());
    }

    @Override
    public void process(LogoutError message) {
        client.notifyObservers(message);
        log.info("processing " + message.getClass());
    }

    @Override
    public void process(UserLoginMessage message) {
        log.info("processing " + message.getClass());
        if (client.loggedIn()) {
            client.addUser(message.getUser());
            client.notifyObservers(message);
        }
    }

    @Override
    public void process(UserLogoutMessage message) {
        log.info("processing " + message.getClass());
        if (client.loggedIn()) {
            client.removeUser(message.getUser());
            client.notifyObservers(message);
        }
    }

    @Override
    public void process(UserMessage message) {
        log.info("processing " + message.getClass());
        if (client.loggedIn()) {
            client.notifyObservers(message);
        }
    }
}
