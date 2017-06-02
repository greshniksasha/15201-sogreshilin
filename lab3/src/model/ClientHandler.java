package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Alexander on 30/05/2017.
 */
public class ClientHandler {
    private final static String SERVER_NAME = "server";

    private String clientName;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;
    private Thread readerThread;
    private Thread writerThread;
    private Server server;

    private static final int CAPACITY = 100;
    private BlockingQueue<Message> queue = new ArrayBlockingQueue<>(CAPACITY);
    private static final Logger log = LogManager.getLogger(Server.class);

    public ClientHandler(Server server, Socket socket) throws IOException {
        writer = new ObjectOutputStream(socket.getOutputStream());
        reader = new ObjectInputStream(socket.getInputStream());
        log.info("got input and output streams");
        this.server = server;
        readerThread = getIncomingMessageReader(socket);
        writerThread = getOutgoingMessageWriter(socket, queue);
        readerThread.start();
        writerThread.start();
        log.info("started reader and writer threads");
    }

    private void broadcast(Message m) {
        for (ClientHandler handler : server.getClientHandlers()) {
            handler.getQueue().add(m);
        }
        addToQueue(m);
    }

    private void addToQueue(Message m) {
        while (!server.getBuffer().offer(m)) {
            server.getBuffer().poll();
        }
    }

    public void registerClient() {
        try {
            while (true) {
                Message message = (Message)reader.readObject();
                if (message.getType() == MessageType.LOGIN) {
                    String nickname = (String)message.getContent();
                    if (server.getParticipants().contains(nickname)) {
                        queue.add(new Message(SERVER_NAME, MessageType.DENY, null));
                        log.info("name is not unique");
                        continue;
                    }
                    server.getParticipants().add(nickname);
                    clientName = nickname;
                    queue.add(new Message(SERVER_NAME, MessageType.SUCCESS, clientName));
                    queue.add(new Message(SERVER_NAME, MessageType.PARTICIPANTS, server.getParticipants()));
                    Message[] messages = new Message[server.getBuffer().size()];
                    Message lastMessages = new Message(SERVER_NAME, MessageType.MESSAGES, server.getBuffer().toArray(messages));
                    queue.add(lastMessages);
                    broadcast(new Message(SERVER_NAME, MessageType.JOINED, clientName));
                    log.info("registered new client : {}", clientName);
                    break;
                }
                if (message.getType() == MessageType.LOGOUT) {
                    this.getQueue().add(message);
                    log.info("deleting participant : {}", message.getSender());
                    log.info("was : {}", server.getParticipants().toString());
                    server.getParticipants().remove(message.getSender());
                    log.info("now : {}", server.getParticipants().toString());
                    if (!clientName.isEmpty()) {
                        broadcast(new Message(SERVER_NAME, MessageType.LEFT, clientName));
                    }
                    break;
                }

            }
        } catch (IOException | ClassNotFoundException e) {
            log.error("error while registering new client", e);
        }
    }

    public BlockingQueue<Message> getQueue() {
        return queue;
    }

    public Thread getIncomingMessageReader(Socket socket) {
        return new Thread(() -> {
            try {
                registerClient();
                while(!Thread.interrupted()) {
                    Message message = (Message)reader.readObject();
                    log.info("message received from {} : {}", message.getSender(), message.getContent());
                    if (message.getType() == MessageType.LOGOUT) {
                        this.getQueue().add(message);
                        log.info("deleting participant : {}", message.getSender());
                        log.info("was : {}", server.getParticipants().toString());
                        server.getParticipants().remove(message.getSender());
                        log.info("now : {}", server.getParticipants().toString());
                        broadcast(new Message(SERVER_NAME, MessageType.LEFT, clientName));
                        break;
                    }
//                    if (message.getType() == MessageType.PARTICIPANTS) {
//                        Message answer = new Message(SERVER_NAME, MessageType.PARTICIPANTS, server.getParticipants());
//                        this.getQueue().add(answer);
//                        continue;
//                    }
                    for (ClientHandler handler : server.getClientHandlers()) {
                        handler.getQueue().add(message);
                    }
                    addToQueue(message);
                }
            } catch (IOException e) {
                log.info("client disconnected : reader thread finished");
            } catch (ClassNotFoundException e) {
                log.error("message of a non-supported format");
            } finally {
                log.info("finished with no exception");
            }
        });
    }

    public Thread getOutgoingMessageWriter(Socket socket, BlockingQueue<Message> queue) {
        return new Thread(() -> {
            try {
                while(!Thread.interrupted()) {
                    Message message = queue.take();
                    writer.writeObject(message);
                    writer.flush();
                    log.info("message sent to {} : {}", clientName, message.getContent());
                    if (message.getType() == MessageType.LOGOUT) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                log.info("interrupted");
            } catch (IOException e) {
                log.info("client disconnected : writer thread finished");
            } finally {
                log.info("finished with no exception");
            }
        });
    }
}
