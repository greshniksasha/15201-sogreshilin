package model.message;

/**
 * Created by Alexander on 06/06/2017.
 */
import model.client.MessageHandler;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Alexander on 06/06/2017.
 */
@XmlRootElement(name="error")
public class TextError implements ServerMessage, Serializable {
    private String error;
    private String text;

    @XmlElement(name = "message")
    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public void process(MessageHandler handler) {
        handler.process(this);
    }
}
