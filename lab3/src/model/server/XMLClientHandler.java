package model.server;

import model.message.*;
import org.xml.sax.SAXException;
import serializing.DOMDeserializer;
import serializing.JAXBSerializer;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Alexander on 07/06/2017.
 */
public class XMLClientHandler extends ClientHandler {

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
                    byte[] data = readData(inputStream);
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
                    } catch (OutOfMemoryError e) {
                        sendOutOfMemoryError();
                        log.error("Message is too big");
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

    private byte[] readData(DataInputStream inputStream) throws IOException {
        int length = inputStream.readInt();
        if(length < 0){
            log.error("blocked user because messageLength is negative : {}", length);
            blockUser();
            return null;
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
}
