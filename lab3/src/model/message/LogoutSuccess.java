package model.message;

import model.MessageHandler;
import view.ClientForm;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Alexander on 06/06/2017.
 */
@XmlRootElement(name = "success")
public class LogoutSuccess implements ServerMessage, Serializable {
    @Override
    public void process(MessageHandler handler) {
        handler.process(this);
    }

    @Override
    public void process(ClientForm form) {
        form.process(this);
    }
}
