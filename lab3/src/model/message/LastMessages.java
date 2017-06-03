package model.message;

import model.MessageHandler;
import view.ClientForm;

import java.io.Serializable;

/**
 * Created by Alexander on 02/06/2017.
 */
public class LastMessages implements ServerMessage, Serializable {
    private DisplayMessage[] messages;

    public LastMessages(DisplayMessage[] messages) {
        this.messages = messages;
    }

    public DisplayMessage[] getMessages() {
        return messages;
    }

    @Override
    public void process(MessageHandler handler) {
        handler.process(this);
    }

    @Override
    public void process(ClientForm form) {
        form.process(this);
    }
}
