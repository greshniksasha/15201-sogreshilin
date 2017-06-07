package model.message;

import model.ClientHandler;
import model.ObjectStreamClientHandler;
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
@XmlType(propOrder = {"name", "chatClientName"})
public class LoginRequest implements ClientMessage, Serializable {
    private String name;
    private String chatClientName;
    @XmlAttribute(name = "name")
    private final Command type = Command.LOGIN;

    public LoginRequest() {
    }

    @XmlElement(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "type")
    public void setChatClientName(String chatClientName) {
        this.chatClientName = chatClientName;
    }

    public String getName() {
        return name;
    }

    public String getChatClientName() {
        return chatClientName;
    }

    @Override
    public void process(Server server, ClientHandler handler) {
        server.process(this, handler);
    }
}
