package model.message;

import model.ClientHandler;
import model.Server;

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
    private String name;
    private String type;
    @XmlAttribute(name = "name")
    private final Command messageType = Command.LOGIN;

    public LoginRequest() {
    }

    @XmlElement(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "type")
    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public void process(Server server, ClientHandler handler) {
        server.process(this, handler);
    }
}
