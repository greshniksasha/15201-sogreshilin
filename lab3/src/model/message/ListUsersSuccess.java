package model.message;

import model.MessageHandler;
import model.User;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Alexander on 02/06/2017.
 */
@XmlRootElement(name="success")
public class ListUsersSuccess implements ServerMessage, Serializable {
    private List<User> users;

    @XmlElementWrapper(name = "listusers")
    @XmlElement(name = "user")
    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    @Override
    public void process(MessageHandler handler) {
        handler.process(this);
    }
}
