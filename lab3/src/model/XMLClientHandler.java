package model;

import model.message.ClientMessage;
import model.message.ServerMessage;
import model.message.TextMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import serializing.DOMDeserializer;
import serializing.JAXBSerializer;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Alexander on 07/06/2017.
 */
public class XMLClientHandler implements ClientHandler {
    private Thread reader;
    private Thread writer;
    private int sessionID;
    private String name;
    private Socket socket;
    private BlockingQueue<ServerMessage> messagesToSend;
    private User user = new User();

    private static final Logger log = LogManager.getLogger(ObjectStreamClientHandler.class);
    private static AtomicInteger sessionIDGenerator = new AtomicInteger(0);
    private static final int QUEUE_CAPACITY = 1000;

    XMLClientHandler(Server server, Socket socket) {
        this.socket = socket;
        sessionID = sessionIDGenerator.getAndIncrement();
        messagesToSend = new ArrayBlockingQueue<ServerMessage>(QUEUE_CAPACITY);
//        user = new User();
//        user.setName("null");
//        user.setType("null");

        reader = new Thread(() -> {
            try {
                DOMDeserializer deserializer = new DOMDeserializer();
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                while (!Thread.interrupted()) {
                    int messageLength = inputStream.readInt();
                    byte[] inputData = new byte[messageLength];
                    if (inputStream.read(inputData, 0, messageLength) != messageLength) {
                        log.error("read less bytes than supposed to");
                    }
                    String data = new String(inputData, StandardCharsets.UTF_8);
                    ClientMessage message = (ClientMessage) deserializer.deserialize(data);
                    message.process(server, this);
                }
            } catch (IOException e) {
                log.info("stopped");
            }
        }, "XMLReader-" + sessionID);

        writer = new Thread(() -> {
            try {
                JAXBSerializer serializer = new JAXBSerializer();
                DataOutputStream writerStream = new DataOutputStream(socket.getOutputStream());
                while(!Thread.interrupted()) {
                    ServerMessage message = messagesToSend.take();
                    String xmlString = serializer.messageToXMLString(message);
                    byte[] data = xmlString.getBytes(StandardCharsets.UTF_8);
                    writerStream.writeInt(data.length);
                    writerStream.write(data);
                    writerStream.flush();
                    log.info("message writen to {}", getName());
                }
            } catch (IOException e) {
                log.error("could not get output stream");
            } catch (InterruptedException e) {
                log.info("interrupted");
            }
        }, "XMLWriter-" + sessionID);

    }

    public void addOutgoingMessage(ServerMessage message) {
        messagesToSend.add(message);
    }

    public String getName() {
        return user.getName();
    }

//    public void setName(String name) {
//        this.name = name;
//    }


    @Override
    public User getUser() {
        return user;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void start() {
        reader.start();
        writer.start();
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    public void stop() {
        writer.interrupt();
    }
}
