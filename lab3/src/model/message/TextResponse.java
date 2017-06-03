package model.message;

import model.MessageHandler;
import view.ClientForm;

import java.io.Serializable;

/**
 * Created by Alexander on 02/06/2017.
 */
public class TextResponse implements ServerMessage, Serializable {
    private Boolean success;
    private String error;

    public TextResponse() {
        success = true;
        error = null;
    }

    public TextResponse(String errorMessage) {
        success = false;
        error = errorMessage;
    }

    public Boolean succeeded() {
        return success;
    }

    public String getError() {
        return error;
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
