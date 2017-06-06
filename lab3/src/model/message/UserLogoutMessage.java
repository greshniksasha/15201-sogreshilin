package model.message;

import model.MessageHandler;
import view.ClientForm;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Alexander on 02/06/2017.
 */
@XmlRootElement(name = "event")
public class UserLogoutMessage implements ServerMessage, DisplayMessage, Serializable {
    private String name;
    @XmlAttribute(name = "name")
    private final Event type = Event.USER_LOGOUT;

    public UserLogoutMessage() {
    }

    @XmlElement(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void process(MessageHandler handler) {
        handler.process(this);
    }

    @Override
    public void process(ClientForm form) {
        form.process(this);
    }

    @Override
    public String messageToShow() {
        return name + " left the chat";
    }
}
