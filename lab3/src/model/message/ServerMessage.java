package model.message;

import model.client.MessageHandler;

/**
 * Created by Alexander on 02/06/2017.
 */
public interface ServerMessage extends Message {
    void process(MessageHandler handler);
}
