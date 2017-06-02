package model;

import java.io.Serializable;

/**
 * Created by Alexander on 30/05/2017.
 */
public class Message implements Serializable {
    private String sender;
    private MessageType type;
    private Object content;

    public Message(String sender, MessageType type, Object content) {
        this.sender = sender;
        this.type = type;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public MessageType getType() {
        return type;
    }

    public Object getContent() {
        return content;
    }
}
