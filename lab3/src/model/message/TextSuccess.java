package model.message;

import model.client.MessageHandler;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Alexander on 06/06/2017.
 */
@XmlRootElement(name = "success")
public class TextSuccess implements ServerMessage, Serializable {

    @Override
    public void process(MessageHandler handler) {
        handler.process(this);
    }
}
