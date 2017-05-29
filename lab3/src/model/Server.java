package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alexander on 29/05/2017.
 */
public class Server {
    private static final short PORT = 5000;
    private ServerSocket serverSocket;
    private List<PrintWriter> clientOutputStreams = new ArrayList();
    private static final Logger log = LogManager.getLogger(Server.class);

    public Server() {
        try {
            this.serverSocket = new ServerSocket(5000);
        } catch (IOException var2) {
            log.error("server socket creating error");
        }

        log.info("server socket created");
    }

    public void go() {
        try {
            while(true) {
                Socket e = this.serverSocket.accept();
                PrintWriter writer = new PrintWriter(e.getOutputStream());
                this.clientOutputStreams.add(writer);
                (new Thread(new Server.ClientHandler(e))).start();
            }
        } catch (IOException var3) {
            log.error("getting client\'s output stream error");
        }
    }

    public static void main(String[] args) {
        (new Server()).go();
    }

    public class ClientHandler implements Runnable {
        private Socket socket;
        BufferedReader reader;

        public ClientHandler(Socket clientSocket) {
            this.socket = clientSocket;

            try {
                this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException var4) {
                Server.log.error("getting client\'s input stream error");
            }

        }

        private void tellEveryone(String message) {
            Iterator var2 = Server.this.clientOutputStreams.iterator();

            while(var2.hasNext()) {
                PrintWriter writer = (PrintWriter)var2.next();
                writer.println(message);
                writer.flush();
            }

        }

        public void run() {
            while(true) {
                try {
                    String message;
                    if((message = this.reader.readLine()) != null) {
                        Server.log.info("received message from {}: {}", this.socket.getInetAddress().toString(), message);
                        this.tellEveryone(message);
                        continue;
                    }
                } catch (IOException var3) {
                    Server.log.error("receiving message from client error");
                }

                return;
            }
        }
    }
}
