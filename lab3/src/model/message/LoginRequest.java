package model.message;

import model.ClientHandler;
import model.Server;
import model.User;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Created by Alexander on 02/06/2017.
 */
@XmlRootElement(name = "command")
@XmlType(propOrder = {"name", "type"})
public class LoginRequest implements ClientMessage, Serializable {
    @XmlAttribute(name = "name")
    private final String messageType = "login";
    private User user;

    private static int MAX_LENGTH = 256;

    public LoginRequest() {
        user = new User();
    }

    @XmlElement(name = "name")
    public void setName(String name) {
        user.setName(name);
    }

    @XmlElement(name = "type")
    public void setType(String type) {
        user.setType(type);
    }

    public String getName() {
        return user.getName();
    }

    public String getType() {
        return user.getType();
    }

    public User getUser() {
        return user;
    }

    @Override
    public void process(Server server, ClientHandler handler) {
        server.process(this, handler);
    }
}
