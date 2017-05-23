package model.contractor;

import model.item.Accessory;
import model.warehouse.Warehouse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.concurrent.atomic.AtomicInteger;

public class AccessorySupplier extends Contractor implements Runnable {

    private Warehouse<Accessory> warehouse;
    private int timeout;
    private static AtomicInteger transactionCounter = new AtomicInteger(0);
    private final Object lock = new Object();

    private static final Logger log = LogManager.getLogger(AccessorySupplier.class);
    private static AtomicInteger threadId = new AtomicInteger(0);

    public AccessorySupplier(Warehouse<Accessory> warehouse, int timeout) {
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

    private void incrementItemsSuppliedCounter() {
        int soldCars = transactionCounter.incrementAndGet();
        notifyTransactionCounterObserver(soldCars);
    }

    public Thread getThread() {
        Thread thread = new Thread(this);
        thread.setName(this.toString() + threadId.getAndIncrement() + "]");
        return thread;
    }

    @Override
    public void run() {
        try {
            log.info("started");
            while(true) {
                Accessory accessory = new Accessory();
                warehouse.put(accessory);
                incrementItemsSuppliedCounter();
                log.info("put item A<" + accessory.getId() + "> in warehouse");
                synchronized (lock) {
                    if (timeout > 0) {
                        lock.wait(timeout);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.info("stopped");
        }
    }

    public String toString() {
        return "AccessorySupplier[";
    }
}
