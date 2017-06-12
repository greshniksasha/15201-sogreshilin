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
public class XMLClientHandler extends ClientHandler {

    private static int BYTE_BUFFER_SIZE = 100000;

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
                    if (data == null) {
                        break;
                    }
                    try {
                        ClientMessage message = (ClientMessage) deserializer.deserialize(data);
                        message.process(server, this);
                    } catch (SAXException e) {
                        log.error("could not deserialize : user send bad data");
                        blockUser();
                        return;
                    }
                }
            } catch (IOException e) {
                log.info("stopped");
                server.clientDisconnected(this);
            } finally {
                log.info("thread finished");
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
                log.error("IO Exception");
            } catch (InterruptedException e) {
                log.info("interrupted");
            } finally {
                log.info("thread finished");
            }
        }, "XMLWriter-" + sessionID);
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
//                log.info("read data : {}", partOfData);
                data += partOfData;
            } while (leftToRead != 0);
        } catch (SocketTimeoutException e) {
            log.error("actual message length is shorter than one in the xml");
        }
        socket.setSoTimeout(0);
        return data;
    }
}
