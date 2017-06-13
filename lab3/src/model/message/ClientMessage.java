package model.message;

import model.server.ClientHandler;
import model.server.Server;

/**
 * Created by Alexander on 03/06/2017.
 */
public interface ClientMessage extends Message {
    void process(Server server, ClientHandler handler);
}
