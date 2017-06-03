package model.message;

import model.Server;

import java.io.Serializable;

/**
 * Created by Alexander on 02/06/2017.
 */
public class LoginRequest implements ClientMessage, Serializable {
    private String name;
    private String chatClientName;

    public LoginRequest(String name, String chatClientName) {
        this.name = name;
        this.chatClientName = chatClientName;
    }

    public String getName() {
        return name;
    }

    public String getChatClientName() {
        return chatClientName;
    }

    @Override
    public void process(Server server, Server.ClientHandler handler) {
        server.process(this, handler);
    }
}
