package model;

import model.message.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Alexander on 29/05/2017.
 */
public class Server {
    private static AtomicInteger sessionIDGenerator = new AtomicInteger(0);
    private static final int BUFFER_CAPACITY = 10;
    private static final int QUEUE_CAPACITY = 100;
    private static final short PORT = 5000;

    private ServerSocket serverSocket;
    private List<String> users;
    private BlockingQueue<DisplayMessage> messageBuffer;
    private BlockingQueue<ServerMessage> serverMessages;
    private List<ClientHandler> clientHandlers;

    private Thread acceptor;
    private Thread sender;
    private final Object lock = new Object();

    private static final Logger log = LogManager.getLogger(Server.class);

    public Server() {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            log.error("server socket creating error");
        }
        messageBuffer = new ArrayBlockingQueue<>(BUFFER_CAPACITY);
        serverMessages = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        clientHandlers = new ArrayList<>();
        users = new ArrayList<>();
        log.info("server socket created");
    }

    public List<String> getUsers() {
        return users;
    }

    public void process(LoginRequest message, ClientHandler handler) {
        String name = message.getName();
        LoginResponse response;
        if (users.contains(name)) {
            response = new LoginResponse("user name is already in use");
            log.info("user name is already in use");
        } else {
            handler.setName(name);
            users.add(name);
            response = new LoginResponse(handler.getSessionID());
            log.info("add new user={} sessionID={}", name, handler.getSessionID());
            serverMessages.add(new UserLoginMessage(name));
        }
        handler.addOutgoingMessage(response);
    }

    public void process(TextMessage message, ClientHandler handler) {
        if (message.getSessionID() != handler.getSessionID()) {
            handler.addOutgoingMessage(new TextResponse("session IDs are not equal"));
            log.info("in text message session IDs are not equal");
            return;
        }
        handler.addOutgoingMessage(new TextResponse());
        log.info("successful text response sent to {}", handler.getName());
        String name = handler.getName();
        String text = message.getText();
        serverMessages.add(new UserMessage(name, text));
        log.info("text message \"{}\" sent to everyone", text);
    }

    public void process(ListUsersRequest message, ClientHandler handler) {
        if (message.getSessionID() != handler.getSessionID()) {
            handler.addOutgoingMessage(new ListUsersResponse("session IDs are not equal"));
            log.info("in list users request session IDs are not equal");
            return;
        }
        handler.addOutgoingMessage(new ListUsersResponse(getUsers()));
        log.info("sent list users {} to {}", getUsers(), handler.getName());
    }

    public void process(LogoutRequest message, ClientHandler handler) {
        if (message.getSessionID() != handler.getSessionID()) {
            handler.addOutgoingMessage(new LogoutResponse("session IDs are not equal"));
            log.info("in logout request session IDs are not equal");
            return;
        }
        handler.addOutgoingMessage(new LogoutResponse());
        log.info("successful logout response sent to user {}", handler.getName());
        users.remove(handler.getName());
        serverMessages.add(new UserLogoutMessage(handler.getName()));
        handler.stop();
        clientHandlers.remove(handler);
        log.info("sent user logout message to everyone");
    }

    private void start() {
        acceptor = new Thread(new Acceptor());
        acceptor.start();

        sender = new Thread(new Sender());
        sender.start();
    }

    private void stop() {
        try {
            serverSocket.close();
            sender.interrupt();
            for (ClientHandler h : clientHandlers) {
                h.stop();
            }
//            acceptor.join();
//            sender.join();
        } catch (IOException e) {
            log.error("error closing socket");
        }
//        } catch (InterruptedException e) {
//            log.info("interrupted");
//        }
    }

    public static void main(String[] args) {
        log.info("to stop server write \"exit\"");
        Server server = new Server();
        server.start();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (br.readLine().compareToIgnoreCase("exit") != 0);
        } catch (IOException e) {
            log.info("io exception", e);
        }
        server.stop();
    }

    public class ClientHandler {
        private Thread reader;
        private Thread writer;
        private int sessionID;
        private String name;
        private Socket socket;
        private BlockingQueue<ServerMessage> messagesToSend;

        ClientHandler(Socket socket) {
            this.socket = socket;
            sessionID = Server.sessionIDGenerator.getAndIncrement();
            messagesToSend = new ArrayBlockingQueue<ServerMessage>(QUEUE_CAPACITY);

            reader = new Thread(() -> {
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                    log.info("got input stream file descriptor");
                    while (!Thread.interrupted()) {
                        ClientMessage m = (ClientMessage)inputStream.readObject();
                        m.process(Server.this, this);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    log.info("stopped");
                }
            }, "Reader-" + sessionID);

            writer = new Thread(() -> {
                try {
                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                    log.info("got output stream file descriptor");
                    while (!Thread.interrupted()) {
                        ServerMessage m = messagesToSend.take();
                        outputStream.writeObject(m);
                        outputStream.flush();
                        log.info("message writen to {}", getName());
                    }
                } catch (IOException e) {
                    log.error("could not get output stream");
                } catch (InterruptedException e) {
                    log.info("interrupted");
                }
            }, "Writer-" + sessionID);

        }

        void addOutgoingMessage(ServerMessage message) {
            messagesToSend.add(message);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSessionID() {
            return sessionID;
        }

        void start() {
            reader.start();
            writer.start();
        }

        void stop() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            reader.interrupt();
            writer.interrupt();
        }
    }

    private class Acceptor implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    Socket socket = serverSocket.accept();
                    ClientHandler handler = new ClientHandler(socket);
                    handler.start();
                    synchronized (lock) {
                        clientHandlers.add(handler);
                    }
                    log.info("new client accepted");
                }
            } catch (IOException e) {
                log.info("stopped");
            }
        }
    }

    private class Sender implements Runnable {
        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    ServerMessage m = serverMessages.take();
                    synchronized (lock) {
                        for (ClientHandler h : clientHandlers) {
                            h.addOutgoingMessage(m);
                        }
                    }
                }
            } catch (InterruptedException e) {
                log.info("interrupted");
            }
        }
    }

}
