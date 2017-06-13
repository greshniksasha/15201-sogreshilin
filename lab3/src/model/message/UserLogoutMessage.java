package model.message;

import model.client.MessageHandler;
import model.User;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Alexander on 02/06/2017.
 */
@XmlRootElement(name = "event")
public class UserLogoutMessage implements ServerMessage, DisplayMessage, Serializable {
    @XmlAttribute(name = "name")
    private final String messageType = "userlogout";
//    private String name;
    private User user;

    public UserLogoutMessage() {
        user = new User();
    }

    @XmlElement(name = "name")
    public void setName(String name) {
        user.setName(name);
    }

    public String getName() {
        return user.getName();
    }

    public User getUser() {
        return user;
    }

    @Override
    public void process(MessageHandler handler) {
        handler.process(this);
    }

    @Override
    public String messageToShow() {
        return "left the chat";
    }
}
