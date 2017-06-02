package model.contractor;

import model.item.Engine;
import model.warehouse.Warehouse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.concurrent.atomic.AtomicInteger;

public class EngineSupplier extends Contractor implements Runnable {

    private Warehouse<Engine> warehouse;
    private int timeout;
    private static AtomicInteger transactionCounter = new AtomicInteger(0);
    private final Object lock = new Object();

    private static final Logger log = LogManager.getLogger(EngineSupplier.class);

    public EngineSupplier(Warehouse<Engine> warehouse, int timeout) {
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

    private void waitSettingTimeout(int millisec) throws InterruptedException {
        synchronized (lock) {
            if (millisec > 0) {
                lock.wait(millisec);
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
                Engine engine = new Engine();
                warehouse.put(engine);
                incrementItemsSuppliedCounter();
                log.info("put item E<{}> in warehouse", engine.getId());
                waitSettingTimeout(timeout);
            }
        } catch (InterruptedException e) {
            log.info("stopped");
        }
    }

    public String toString() {
        return "EngineSupplier";
    }

}
