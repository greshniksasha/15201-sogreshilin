package model.message;

import model.MessageHandler;
import model.Server;

import java.io.Serializable;

/**
 * Created by Alexander on 02/06/2017.
 */
public class TextMessage implements ClientMessage, Serializable {
    private String text;
    private int sessionID;

    public TextMessage(String text, int sessionID) {
        this.text = text;
        this.sessionID = sessionID;
    }

    public String getText() {
        return text;
    }

    public int getSessionID() {
        return sessionID;
    }

    @Override
    public void process(Server server, Server.ClientHandler handler) {
        server.process(this, handler);
    }
}
