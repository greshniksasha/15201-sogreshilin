package model.message;

import model.MessageHandler;
import view.ClientForm;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Created by Alexander on 02/06/2017.
 */
@XmlRootElement(name = "event")
public class UserLoginMessage implements ServerMessage, DisplayMessage, Serializable {
    private String name;
    private String type;
    @XmlAttribute(name = "name")
    private final Event messageType = Event.USER_LOGIN;

    public UserLoginMessage() {
    }

    @XmlElement(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "type")
    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
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
        return name + " joined the chat";
    }
}
