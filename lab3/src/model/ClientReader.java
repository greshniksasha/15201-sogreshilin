package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.LoginForm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 01/06/2017.
 */
public class ClientReader implements Runnable {

    private ObjectInputStream reader;
    private List<String> participants;
    private Client client;
    private List<IncomingMessageObserver> messageObservers;
    private List<ParticipantsObserver> participantsObservers;
    private static final Logger log = LogManager.getLogger(Client.class);

    public ClientReader(Client client, ObjectInputStream reader) {
        this.client = client;
        this.reader = reader;
        this.participants = new ArrayList<>();
        this.messageObservers = new ArrayList<>();
        this.participantsObservers = new ArrayList<>();
    }

    public void addIncomingMessageListener(IncomingMessageObserver listener) {
        this.messageObservers.add(listener);
        log.info("listener added");
    }

    public void removeIncomingMessageListener(IncomingMessageObserver listener) {
        this.messageObservers.remove(listener);
        log.info("listener removed");
    }

    private void addParticipant(String participant) {
        if (!participants.contains(participant)) {
            participants.add(participant);
            notifyParticipantsObservers();
        }
    }

    private void removeParticipant(String participant) {
        participants.remove(participant);
        notifyParticipantsObservers();
    }

    public void addParticipantsObserver(ParticipantsObserver o) {
        participantsObservers.add(o);
    }

    private void notifyParticipantsObservers() {
        for (ParticipantsObserver observer : participantsObservers) {
            observer.handle(participants);
        }
    }

    private void receiveParticipants() throws IOException, ClassNotFoundException {
        Message message = (Message)reader.readObject();
        while (true) {
            if (message.getType() == MessageType.PARTICIPANTS) {
                for (String participant : (List<String>) message.getContent()) {
                    addParticipant(participant);
                }
                break;
            }
        }
    }

    private void receiveLastMessages() throws IOException, ClassNotFoundException {
        Message message = (Message)reader.readObject();
        while (true) {
            if (message.getType() == MessageType.MESSAGES) {
                for (Message m : (Message[])message.getContent()) {
                    handleMessage(m);
                }
                break;
            }
        }
    }

    private void handleMessage(Message message) {
        if (message.getType() == MessageType.LOGOUT) {
            Thread.currentThread().interrupt();
        }
        if (message.getType() == MessageType.PARTICIPANTS) {
            log.info("got participants : {}", message.getContent());
        }
        if (message.getType() == MessageType.JOINED) {
            addParticipant((String)message.getContent());
        }
        if (message.getType() == MessageType.LEFT) {
            removeParticipant((String)message.getContent());
        }
        for (IncomingMessageObserver listener : messageObservers) {
            listener.handle(message);
        }
    }

    public Client getClient() {
        return client;
    }

    private void login() throws IOException, ClassNotFoundException {
        LoginForm form = new LoginForm(this);
        form.setVisible(true);
        log.info("Started choosing the name");
        while (true) {
            Message message = (Message)reader.readObject();
            switch (message.getType()) {
                case SUCCESS: {
                    client.setNickname((String)message.getContent());
                    for (IncomingMessageObserver listener : messageObservers) {
                        listener.handle(message);
                    }
                    log.info("successfully logged in");
                    form.startChatForm();
                    return;
                }
                case DENY: {
                    for (IncomingMessageObserver listener : messageObservers) {
                        listener.handle(message);
                    }
                    log.info("name is used, try another one");
                    break;
                }
                default: {
                    log.info("do not care about messages of this type");
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            login();
            receiveParticipants();
            receiveLastMessages();
            while(!Thread.interrupted()) {
                Message message = (Message)reader.readObject();
                log.info("got message from {} : {}", message.getSender(), message.getContent());
                handleMessage(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            log.info("got signal that socket closed");
        } finally {
            log.info("finished");
        }
    }


    public interface IncomingMessageObserver {
        void handle(Message message);
    }

    public interface ParticipantsObserver {
        void handle(List<String> participants);
    }
}
