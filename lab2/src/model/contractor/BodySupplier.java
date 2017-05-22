package model.contractor;

import model.item.Body;
import model.warehouse.Warehouse;
import org.apache.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public class BodySupplier extends Contractor implements Runnable {

    private Warehouse<Body> warehouse;
    private int timeout;
    private static AtomicInteger transactionCounter = new AtomicInteger(0);
    private final Object lock = new Object();

    private static final Logger log = Logger.getLogger(BodySupplier.class);

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
        log.trace("Body contractor timeout changed to " + timeout);
    }

    private void incrementItemsSuppliedCounter() {
        int soldCars = transactionCounter.incrementAndGet();
        notifyTransactionCounterObserver(soldCars);
    }

    public Thread getThread() {
        Thread thread = new Thread(this);
        thread.setName(this.toString() + "0");
        return thread;
    }

    @Override
    public void run() {
        try {
            while(true) {
                Body body = new Body();
                warehouse.put(body);
                incrementItemsSuppliedCounter();
                log.info(Thread.currentThread().getName() +
                        " : Accessory <" + body.getId() + ">");
                synchronized (lock) {
                    if (timeout > 0) {
                        lock.wait(timeout);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.trace(Thread.currentThread().getName() + " stopped");
            return;
        }
    }

    public String toString() {
        return "Supplier<Body>-";
    }

}
