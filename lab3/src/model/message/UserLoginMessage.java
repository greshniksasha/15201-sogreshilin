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
public class UserLoginMessage implements ServerMessage, DisplayMessage, Serializable {
    @XmlAttribute(name = "name")
    private final String messageType = "userlogin";
//    private String name;
//    private String type;
    private User user;

    public UserLoginMessage() {
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

    public String getType() {
        return user.getType();
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
        return "joined the chat";
    }
}
