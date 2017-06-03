package model.message;

import model.MessageHandler;
import model.Server;
import view.ClientForm;

/**
 * Created by Alexander on 02/06/2017.
 */
public interface ServerMessage {
    void process(MessageHandler handler);
    void process(ClientForm form);
}
