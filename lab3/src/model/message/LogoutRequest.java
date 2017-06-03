package model.message;

import model.MessageHandler;
import model.Server;

import java.io.Serializable;

/**
 * Created by Alexander on 02/06/2017.
 */
public class LogoutRequest implements ClientMessage, Serializable {
    private int sessionID;

    public LogoutRequest(int sessionID) {
        this.sessionID = sessionID;
    }

    public int getSessionID() {
        return sessionID;
    }

    @Override
    public void process(Server server, Server.ClientHandler handler) {
        server.process(this, handler);
    }
}
