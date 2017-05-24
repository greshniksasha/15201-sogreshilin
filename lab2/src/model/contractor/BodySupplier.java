package model.contractor;

import model.item.Body;
import model.warehouse.Warehouse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.concurrent.atomic.AtomicInteger;

public class BodySupplier extends Contractor implements Runnable {

    private Warehouse<Body> warehouse;
    private int timeout;
    private static AtomicInteger transactionCounter = new AtomicInteger(0);
    private final Object lock = new Object();

    private static final Logger log = LogManager.getLogger(BodySupplier.class);

    public BodySupplier(Warehouse<Body> warehouse, int timeout) {
        this.warehouse = warehouse;
        this.timeout = timeout;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        synchronized (lock) {
            this.timeout = timeout;
            lock.notify();
        }
        log.info("timeout changed");
    }

    private void waitSettingTimeout(int milliseconds) throws InterruptedException {
        synchronized (lock) {
            if (milliseconds > 0) {
                lock.wait(milliseconds);
            }
        }
    }

    private void incrementItemsSuppliedCounter() {
        int soldCars = transactionCounter.incrementAndGet();
        notifyTransactionCounterObserver(soldCars);
    }

    public Thread getThread() {
        Thread thread = new Thread(this);
        thread.setName(this.toString());
        return thread;
    }

    @Override
    public void run() {
        try {
            log.info("started");
            while(!Thread.interrupted()) {
                Body body = new Body();
                warehouse.put(body);
                incrementItemsSuppliedCounter();
                log.info("put item B<{}> in warehouse", body.getId());
                waitSettingTimeout(timeout);
            }
        } catch (InterruptedException e) {
            log.info("stopped");
        }
    }

    public String toString() {
        return "BodySupplier";
    }

}
