package model.message;

import model.MessageHandler;
import view.ClientForm;

import java.io.Serializable;

/**
 * Created by Alexander on 03/06/2017.
 */
public class UserMessage implements ServerMessage, DisplayMessage, Serializable {
    private String name;
    private String message;

    public UserMessage(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
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
        return name + ": " + message;
    }
}
