package model.client;

import model.User;
import model.message.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.xml.sax.SAXException;
import serializing.DOMDeserializer;
import serializing.JAXBSerializer;
import view.WelcomeForm;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Alexander on 29/05/2017.
 */
public class Client {
    private static int TIMEOUT = 5000;
    private static final String CONFIG_FILE_PATH = "src/client_config.properties";
    private static final String OBJ = "obj";
    private static final String XML = "xml";
    private Socket socket;
    private int sessionID;
    private User user;
    private Boolean loggedIn;
    private BlockingQueue<ClientMessage> messagesToSend;
    private BlockingQueue<TextMessage> textMessages;
    private BlockingQueue<Class> sentMessageTypes;
    private List<User> users;
    private List<IncomingMsgObserver> observers;
    private ConnectionObserver connectionObserver;
    private int port;
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
        sentMessageTypes = new ArrayBlockingQueue<>(CAPACITY);
        messageHandler = new ClientMessageHandler(this);
        messagesToSend = new ArrayBlockingQueue<>(CAPACITY);
        textMessages = new ArrayBlockingQueue<>(CAPACITY);
        observers = new ArrayList<>();
        loggedIn = false;
        users = new ArrayList<>();
        port = clientConfigs.getPort();
        ip = clientConfigs.getIp();
    }

    public void connectToServer() {
        try {
            log.info("IP = {}; PORT = {}", ip, port);
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), TIMEOUT);
        } catch (SocketTimeoutException e) {
            log.error("timeout expired");
            connectionObserver.handle(false);
            return;
        } catch (IOException e) {
            log.error("connecting to server error");
            connectionObserver.handle(false);
            return;
        }
        if (connectionObserver != null) {
            connectionObserver.handle(true);
        }
        log.info("connected to server");
        switch (user.getType()) {
            case OBJ:
                this.objectStreamsGo();
                log.info("Connected to ObjectStream Server");
                return;
            case XML:
                this.xmlGo();
                log.info("Connected to XML Server");
        }
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

    public interface ConnectionObserver {
        void handle(Boolean connected);
    }

    public void setConnectionObserver(ConnectionObserver connectionObserver) {
        this.connectionObserver = connectionObserver;
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

    public void joinThreads() throws InterruptedException {
        readerThread.join();
        writerThread.join();
        disconnectFromServer();
    }

    public static void main(String[] args) {
        ClientConfigs clientConfigs = new ClientConfigs(CONFIG_FILE_PATH);
        if (!clientConfigs.getLogOn()) {
            Configurator.setRootLevel(Level.OFF);
        }
        new WelcomeForm(clientConfigs).setVisible(true);
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
                log.info("socket has been closed");
            } finally {
                log.info("thread finished");
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
                    log.info("already read message, processing...");
                    message.process(messageHandler);
                }
            } catch (IOException | ClassNotFoundException e) {
                log.info("socket has been closed");
                connectionObserver.handle(false);
                writerThread.interrupt();
            } finally {
                log.info("thread finished");
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
            } finally {
                log.info("thread finished");
            }
        }
    }

    private byte[] readData(DataInputStream inputStream) throws IOException {
        int length = inputStream.readInt();
        if(length < 0){
            throw new IOException();
        }
        int read = 0;
        byte[] buffer = new byte[length];
        socket.setSoTimeout(1000);
        try {
            while(read != length) {
                int temp = inputStream.read(buffer, read, length - read);
                read += temp;
            }
        } catch (SocketTimeoutException e) {
            log.error("actual message length is shorter than one in the xml");
        }
        socket.setSoTimeout(0);
        return buffer;
    }

    private class XMLReader implements Runnable {
        @Override
        public void run() {
            try {
                DOMDeserializer deserializer = new DOMDeserializer();
                deserializer.setQueue(sentMessageTypes);
                DataInputStream readerStream = new DataInputStream(socket.getInputStream());
                while(!Thread.interrupted()) {
                    byte[] data = readData(readerStream);
                    ServerMessage message = (ServerMessage)deserializer.deserialize(data);
                    message.process(messageHandler);
                }
            } catch (IOException e) {
                log.info("IO Exception");
                disconnectFromServer();
                writerThread.interrupt();
                connectionObserver.handle(false);
            } catch (SAXException e) {
                log.info("could not deserialize data from server");
            } finally {
                log.info("thread finished");
            }
        }
    }
}
