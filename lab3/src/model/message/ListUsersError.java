package model.message;

import model.client.MessageHandler;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Alexander on 06/06/2017.
 */
@XmlRootElement(name="error")
public class ListUsersError implements ServerMessage, Serializable {
    private String error;

    @XmlElement(name = "message")
    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    @Override
    public void process(MessageHandler handler) {
        handler.process(this);
    }
}
