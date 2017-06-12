package model.message;

import model.server.ClientHandler;
import model.server.Server;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Created by Alexander on 02/06/2017.
 */
@XmlRootElement(name = "command")
@XmlType(propOrder = {"text", "sessionID"})
public class TextMessage implements ClientMessage, Serializable {
    @XmlAttribute(name = "name")
    private final String messageType = "message";
    private String text;
    private int sessionID;

    public TextMessage() {
    }

    @XmlElement(name = "message")
    public void setText(String text) {
        this.text = text;
    }

    @XmlElement(name = "session")
    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public String getText() {
        return text;
    }

    public int getSessionID() {
        return sessionID;
    }

    @Override
    public void process(Server server, ClientHandler handler) {
        server.process(this, handler);
    }
}
