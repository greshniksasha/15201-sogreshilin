package model;

import model.message.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import serializing.DOMDeserializer;
import serializing.JAXBSerializer;
import view.ClientForm;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Alexander on 29/05/2017.
 */
public class Client {
    private static final String CONFIG_FILE_PATH = "src/client_config.properties";
    private static final String OBJ = "obj";
    private static final String XML = "xml";
    private Socket socket;
    private User user;
//    private String name;
//    private final String type;
    private int sessionID;
    private Boolean loggedIn;
    private BlockingQueue<ClientMessage> messagesToSend;
    private BlockingQueue<TextMessage> textMessages;
    private BlockingQueue<Class> sentMessageTypes;
    private List<User> users;
    private List<IncomingMsgObserver> observers;
    private short port;
    private String ip;

    private MessageHandler messageHandler;
    private Thread readerThread;
    private Thread writerThread;

    private static final int CAPACITY = 100000;
    private static final Logger log = LogManager.getLogger(Client.class);

    static {
        System.getProperties().setProperty("log4j.configurationFile", "src/log4j2.xml");
    }

    public Client(ClientConfigs clientConfigs) {
        user = new User();
        user.setType(clientConfigs.getType());
//        type = clientConfigs.getType();
        sentMessageTypes = new ArrayBlockingQueue<>(CAPACITY);
        messageHandler = new ClientMessageHandler(this);
        messagesToSend = new ArrayBlockingQueue<>(CAPACITY);
        textMessages = new ArrayBlockingQueue<TextMessage>(CAPACITY);
        observers = new ArrayList<>();
        loggedIn = false;
        users = new ArrayList<>();
        port = clientConfigs.getPort();
        ip = clientConfigs.getIp();
    }

    public void connectToServerObjectStream() {
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            log.error("connecting to server error");
        }
        log.info("connected to server");
    }

    public void connectToServerXML() {
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            log.error("connecting to server error");
        }
        log.info("connected to server");
    }

    public void disconnectFromServer() {
        try {
            socket.close();
        } catch (IOException e) {
            log.error("closing socket error");
        }
    }

    public Boolean isConnectedToServer() {
        return !socket.isClosed();
    }

    public Boolean loggedIn() {
        return loggedIn;
    }

    public void loggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void objectStreamsGo() {
            readerThread = new Thread(new ObjectReader(), "ObjectReader");
            writerThread = new Thread(new ObjectWriter(), "ObjectWriter");
            readerThread.start();
            writerThread.start();
    }

    public void xmlGo() {
        readerThread = new Thread(new XMLReader(), "XMLReader");
        writerThread = new Thread(new XMLWriter(), "XMLWriter");
        readerThread.start();
        writerThread.start();
    }


    public List<User> getUsers() {
        return users;
    }

    public void addOutgoingMessage(ClientMessage message) {
        messagesToSend.add(message);
        log.info("message added to the messagesToSend");
    }

    public String getType() {
        return user.getType();
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public void setName(String name) {
        user.setName(name);
    }

    public String getName() {
        return user.getName();
    }

    public int getSessionID() {
        return sessionID;
    }

    public void addUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
        }
    }

    public void addObserver(IncomingMsgObserver o) {
        observers.add(o);
    }

    public void notifyObservers(ServerMessage message) {
        for (IncomingMsgObserver o : observers) {
            o.process(message);
        }
    }

    public interface IncomingMsgObserver {
        void process(ServerMessage message);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public void removeAllUsers() {
        users.clear();
    }

    public void finish() {
        readerThread.interrupt();
        writerThread.interrupt();
        disconnectFromServer();
    }

    public void connectToServer() {
        switch (user.getType()) {
            case OBJ:
                this.connectToServerObjectStream();
                this.objectStreamsGo();
                log.info("Connected to ObjectStream Server");
                return;
            case XML:
                this.connectToServerXML();
                this.xmlGo();
                log.info("Connected to XML Server");
        }
    }

    public static void main(String[] args) {
        ClientConfigs clientConfigs = new ClientConfigs(CONFIG_FILE_PATH);
        if (!clientConfigs.getLogOn()) {
            Configurator.setRootLevel(Level.OFF);
        }
        Client client = new Client(clientConfigs);
        client.connectToServer();
        new ClientForm(client).setVisible(true);

//        new Thread(() -> {
//            try {
//                Thread.sleep(7000);
//                TextMessage message = new TextMessage();
//                message.setSessionID(client.getSessionID());
//                for (int i = 0; i < 1000; ++i) {
//                    message.setText("message-" + i);
//                    client.addOutgoingMessage(message);
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();
    }

    private class ObjectWriter implements Runnable {
        @Override
        public void run() {
            try {
                ObjectOutputStream writerStream = new ObjectOutputStream(socket.getOutputStream());
                while(!Thread.interrupted()) {
                    ClientMessage message = messagesToSend.take();
                    writerStream.writeObject(message);
                    writerStream.flush();
                    if (message instanceof TextMessage) {
                        textMessages.add((TextMessage) message);
                    }
                    log.info("client message written to server");
                }
            } catch (InterruptedException e) {
                log.info("interrupted");
            } catch (IOException e) {
                log.info("could not write message");
            }
        }
    }

    private class ObjectReader implements Runnable {
        @Override
        public void run() {
            try {
                ObjectInputStream readerStream = new ObjectInputStream(socket.getInputStream());
                while(!Thread.interrupted()) {
                    ServerMessage message = (ServerMessage)readerStream.readObject();
                    System.out.println("already read message, processing...");
                    message.process(messageHandler);
                }
            } catch (IOException | ClassNotFoundException e) {
                log.info("socket closed");
            } finally {
                log.info("finished");
            }
        }
    }

    public TextMessage takeTextMessage() {
        try {
            return textMessages.take();
        } catch (InterruptedException e) {
            log.error("interrupted");
        }
        return null;
    }

    private class XMLWriter implements Runnable {
        @Override
        public void run() {
            try {
                JAXBSerializer serializer = new JAXBSerializer();
                DataOutputStream writerStream = new DataOutputStream(socket.getOutputStream());
                while(!Thread.interrupted()) {
                    ClientMessage message = messagesToSend.take();
                    String xmlString = serializer.messageToXMLString(message);
                    byte[] data = xmlString.getBytes(StandardCharsets.UTF_8);
                    writerStream.writeInt(data.length);
                    writerStream.write(data);
                    writerStream.flush();
                    sentMessageTypes.add(message.getClass());
                    if (message instanceof TextMessage) {
                        textMessages.add((TextMessage) message);
                    }
                    log.info("client message written to server");
                }
            } catch (InterruptedException e) {
                log.info("interrupted");
            } catch (IOException e) {
                log.info("could not write message");
            }
        }
    }

    private class XMLReader implements Runnable {
        @Override
        public void run() {
            try {
                DOMDeserializer deserializer = new DOMDeserializer();
                deserializer.setQueue(sentMessageTypes);
                DataInputStream readerStream = new DataInputStream(socket.getInputStream());
                while(!Thread.interrupted()) {
                    int messageLength = readerStream.readInt();
                    byte[] inputData = new byte[messageLength];
                    if (readerStream.read(inputData, 0, messageLength) != messageLength) {
                        log.error("read less bytes than supposed to");
                    }
                    String data = new String(inputData, StandardCharsets.UTF_8);
                    ServerMessage message = (ServerMessage)deserializer.deserialize(data);
                    message.process(messageHandler);
                }
            } catch (IOException e) {
                log.info("socket closed");
            } finally {
                log.info("finished");
            }
        }
    }


}
