package model.message;


import model.MessageHandler;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Alexander on 02/06/2017.
 */
@XmlRootElement(name = "success")
public class LoginSuccess implements ServerMessage, Serializable {
    private Integer sessionID;

    public LoginSuccess() {
    }

    @XmlElement(name = "session")
    public void setSessionID(Integer sessionID) {
        this.sessionID = sessionID;
    }

    public Integer getSessionID() {
        return sessionID;
    }

    @Override
    public void process(MessageHandler handler) {
        handler.process(this);
    }
}
