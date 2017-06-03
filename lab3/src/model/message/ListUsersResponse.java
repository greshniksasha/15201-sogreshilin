package model.message;

import model.MessageHandler;
import view.ClientForm;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Alexander on 02/06/2017.
 */
public class ListUsersResponse implements ServerMessage, Serializable {
    private Boolean success;
    private List<String> users;
    private String error;

    public ListUsersResponse(List<String> userList) {
        success = true;
        users = userList;
        error = null;
    }

    public ListUsersResponse(String errorMessage) {
        success = false;
        users = null;
        error = errorMessage;
    }

    public List<String> getUsers() {
        return users;
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
