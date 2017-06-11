package model;

import model.message.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;
import serializing.DOMDeserializer;
import serializing.JAXBSerializer;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
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
    private Server server;
    private Socket socket;
    private BlockingQueue<ServerMessage> messagesToSend;
    private User user = new User();

    private static final Logger log = LogManager.getLogger(ObjectStreamClientHandler.class);
    private static AtomicInteger sessionIDGenerator = new AtomicInteger(0);
    private static final int QUEUE_CAPACITY = 1000;
    private static int BYTE_BUFFER_SIZE = 1000;

    XMLClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        sessionID = sessionIDGenerator.getAndIncrement();
        messagesToSend = new ArrayBlockingQueue<ServerMessage>(QUEUE_CAPACITY);

        reader = new Thread(() -> {
            try {
                DOMDeserializer deserializer = new DOMDeserializer();
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                while (!Thread.interrupted()) {
                    String data = readData(inputStream);
                    log.info("read data : \n {}", data);
                    if (data == null) {
                        break;
                    }
                    ClientMessage message = null;
                    try {
                        message = (ClientMessage) deserializer.deserialize(data);
                    } catch (SAXException e) {
                        log.error("could not deserialize : user send bad data");
                        blockUser();
                        return;
                    }
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
                    System.out.println(xmlString);
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

    private void blockUser() throws IOException {
        if (!server.getUsers().isEmpty()) {
            server.getUsers().remove(user);
        }
        UserLogoutMessage msg = new UserLogoutMessage();
        msg.setName(getName());
        server.getServerMessages().add(msg);
        log.info("sent user logout message to everyone");
        new Thread(() -> {server.removeClientHandler(this);}).start();
        writer.interrupt();
        log.info("blocked user");
        closeSocket();
    }

    private String readData(DataInputStream inputStream) throws IOException {
        int messageLength = inputStream.readInt();
        log.info("message length : {}", messageLength);
        if (messageLength <= 0) {
            log.error("blocked user because messageLength is negative : {}", messageLength);
            blockUser();
            return null;
        }
        int leftToRead = messageLength;
        String data = "";
        socket.setSoTimeout(1000);
        try {
            do {
                byte[] inputData = new byte[Integer.min(BYTE_BUFFER_SIZE, leftToRead)];
                leftToRead -= inputStream.read(inputData, 0, Integer.min(leftToRead, BYTE_BUFFER_SIZE));
                String partOfData = new String(inputData, StandardCharsets.UTF_8);
                log.info("part of data : \n {}", partOfData);
                data += partOfData;
            } while (leftToRead != 0);
        } catch (SocketTimeoutException e) {
            log.error("actual message length is shorter than one in the xml");
        }
        socket.setSoTimeout(0);


        return data;
    }

    public void addOutgoingMessage(ServerMessage message) {
        messagesToSend.add(message);
    }

    public String getName() {
        return user.getName();
    }

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
    public void closeSocket() throws IOException {
        socket.close();
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    public void interruptWriter() {
        writer.interrupt();
    }
}
