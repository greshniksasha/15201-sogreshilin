package model.message;

import model.ClientHandler;
import model.Server;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Alexander on 02/06/2017.
 */
@XmlRootElement(name = "command")
public class ListUsersRequest implements ClientMessage, Serializable {
    private int sessionID;
    @XmlAttribute(name = "name")
    private final Command type = Command.LIST;

    public ListUsersRequest() {
    }

    @XmlElement(name = "session")
    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public int getSessionID() {
        return sessionID;
    }

    @Override
    public void process(Server server, ClientHandler handler) {
        server.process(this, handler);
    }
}
