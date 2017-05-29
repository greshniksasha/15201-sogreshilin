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
            label21:
            while(true) {
                try {
                    String message;
                    if((message = this.reader.readLine()) != null) {
                        Iterator e = this.listeners.iterator();

                        while(true) {
                            if(!e.hasNext()) {
                                continue label21;
                            }

                            Client.IncomingMessageListener listener = (Client.IncomingMessageListener)e.next();
                            listener.newIncomingMessage(message);
                        }
                    }
                } catch (IOException var4) {
                    var4.printStackTrace();
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
                        Object var2 = this.lock;
                        String e;
                        synchronized(this.lock) {
                            this.lock.wait();
                            e = this.outcoming;
                        }

                        this.writer.println(e);
                        this.writer.flush();
                        continue;
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
        Object var2 = this.lock;
        synchronized(this.lock) {
            this.outcoming = outcoming;
            this.lock.notify();
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
        void newIncomingMessage(String var1);
    }
}
