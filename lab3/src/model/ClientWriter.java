package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Alexander on 01/06/2017.
 */
public class ClientWriter implements Runnable{

    private ObjectOutputStream writer;
    private BlockingQueue<Message> queue;
    private static final Logger log = LogManager.getLogger(Client.class);

    public ClientWriter(ObjectOutputStream writer, BlockingQueue<Message> queue) {
        this.writer = writer;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while(!Thread.interrupted()) {
                Message message = queue.take();
                writer.writeObject(message);
                writer.flush();
                if (message.getType() == MessageType.LOGOUT) {
                    log.info("logout request sent");
                    break;
                }
                log.info("message written to server : {}", message.getContent());
            }
        } catch (InterruptedException e) {
            log.info("interrupted");
        } catch (IOException e) {
            log.info("could not write message");
        } finally {
            log.info("finished");
        }
    }
}
