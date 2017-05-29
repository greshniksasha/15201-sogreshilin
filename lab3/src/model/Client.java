package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.ClientForm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alexander on 29/05/2017.
 */
public class Client {
    private String outcoming;
    private PrintWriter writer;
    private BufferedReader reader;
    private Socket socket;
    private List<IncomingMessageListener> listeners = new ArrayList();
    private final Object lock = new Object();
    private static final String IP = "localhost";
    private static final short PORT = 5000;
    private static final Logger log = LogManager.getLogger(Client.class);

    public Client() {
    }

    public void setupNetworking() {
        try {
            this.socket = new Socket("localhost", 5000);
            this.writer = new PrintWriter(this.socket.getOutputStream());
            this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            log.info("networking established");
        } catch (IOException var2) {
            log.error("connection could not be established");
        }

    }

    public void setupIncomingReader() {
        Thread thread = new Thread(() -> {
            while(true) {
                try {
                    String message;
                    if((message = this.reader.readLine()) != null) {
                        for (IncomingMessageListener listener : listeners) {
                            listener.newIncomingMessage(message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        });
        thread.start();
    }

    public void setupOutcomingWriter() {
        Thread thread = new Thread(() -> {
            while(true) {
                try {
                    if(!Thread.interrupted()) {
                        String message;
                        synchronized(lock) {
                            lock.wait();
                            message = outcoming;
                        }

                        this.writer.println(message);
                        this.writer.flush();
                    }
                } catch (InterruptedException var5) {
                    log.info("stopped");
                }

                return;
            }
        });
        thread.start();
    }

    public void setOutcoming(String outcoming) {
        synchronized(lock) {
            this.outcoming = outcoming;
            lock.notify();
        }
    }

    public void addIncomingMessageListener(Client.IncomingMessageListener listener) {
        this.listeners.add(listener);
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.setupNetworking();
        client.setupIncomingReader();
        client.setupOutcomingWriter();
        ClientForm form = new ClientForm(client);
        form.setVisible(true);
    }

    public interface IncomingMessageListener {
        void newIncomingMessage(String message);
    }
}
