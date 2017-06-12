package model;

import model.message.ClientMessage;
import model.message.ServerMessage;
import model.message.TextError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Alexander on 07/06/2017.
 */
public class ObjectStreamClientHandler extends ClientHandler {

    ObjectStreamClientHandler(Server server, Socket socket) {
        this.socket = socket;
        this.server = server;
        sessionID = sessionIDGenerator.getAndIncrement();
        messagesToSend = new ArrayBlockingQueue<ServerMessage>(QUEUE_CAPACITY);

        reader = new Thread(() -> {
            try {
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                log.info("got input stream file descriptor");
                while (!Thread.interrupted()) {
                    try {
                        ClientMessage m = (ClientMessage) inputStream.readObject();
                        log.info("object is read");
                        m.process(server, this);
                    } catch (ClassNotFoundException | ClassCastException e) {
                        log.info("message of unknown type was sent");
                        log.info("blocking this user");
                        blockUser();
                    }
                }
            } catch (IOException e) {
                log.info("socket has been closed");
                server.clientDisconnected(this);
            }  finally {
                log.info("thread finished");
            }
        }, "ObjectReader-" + sessionID);

        writer = new Thread(() -> {
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                log.info("got output stream file descriptor");
                while (!Thread.interrupted()) {
                    ServerMessage m = messagesToSend.take();
                    outputStream.writeObject(m);
                    outputStream.flush();
                    log.info("message written to {}", getName());
                }
            } catch (IOException e) {
                log.error("socket has been closed");
            } catch (InterruptedException e) {
                log.info("interrupted");
            } finally {
                log.info("thread finished");
            }
        }, "ObjectWriter-" + sessionID);

    }
}

