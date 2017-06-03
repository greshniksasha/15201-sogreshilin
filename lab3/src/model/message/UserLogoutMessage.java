package model.message;

import model.MessageHandler;
import view.ClientForm;

import java.io.Serializable;

/**
 * Created by Alexander on 02/06/2017.
 */
public class UserLogoutMessage implements ServerMessage, DisplayMessage, Serializable {
    private String name;

    public UserLogoutMessage(String name) {
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
