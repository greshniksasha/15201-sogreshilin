package model.message;


import model.MessageHandler;
import view.ClientForm;

import java.io.Serializable;

/**
 * Created by Alexander on 02/06/2017.
 */
public class LoginResponse implements ServerMessage, Serializable {
    private Boolean success;
    private Integer sessionID;
    private String error;

    public LoginResponse(int id) {
        success = true;
        sessionID = id;
        error = null;
    }

    public LoginResponse(String errorMessage) {
        success = false;
        sessionID = null;
        error = errorMessage;
    }

    public Integer getSessionID() {
        return sessionID;
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
