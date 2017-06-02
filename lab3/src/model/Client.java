package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.LoginForm;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Alexander on 29/05/2017.
 */
public class Client {
    private static final String IP = "localhost";
    private static final short PORT = 5000;
    private String nickname = "unknown";
    private ClientReader clientReader;
    private ClientWriter clientWriter;
    private Thread readerThread;
    private Thread writerThread;

    private BlockingQueue<Message> queue;
    private static final int CAPACITY = 100;
    private static final Logger log = LogManager.getLogger(Client.class);

    public Client() {
        this.queue = new ArrayBlockingQueue<Message>(CAPACITY);
        try (Socket socket = new Socket(IP, PORT)) {
            log.info("networking established");

            ObjectInputStream readerStream = new ObjectInputStream(socket.getInputStream());
            clientReader = new ClientReader(this, readerStream);

            ObjectOutputStream writerStream = new ObjectOutputStream(socket.getOutputStream());
            clientWriter = new ClientWriter(writerStream, queue);

            readerThread = new Thread(clientReader, "Reader");
            writerThread = new Thread(clientWriter, "Writer");

            readerThread.start();
            writerThread.start();

            try {
                readerThread.join();
                writerThread.join();
                log.info("both threads died");
            } catch (InterruptedException e) {
                log.info("interrupted");
            }
        } catch (IOException ex) {
            log.error("connection could not be established");
        }
    }

    public void addOutgoingMessage(MessageType type, String content) {
        queue.add(new Message(nickname, type, content));
        log.info("message added to the queue : {}", content);
    }

    public ClientReader getClientReader() {
        return clientReader;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void finish() {
        readerThread.interrupt();
        writerThread.interrupt();
    }

    public static void main(String[] args) {
        Client client = new Client();
    }


}
