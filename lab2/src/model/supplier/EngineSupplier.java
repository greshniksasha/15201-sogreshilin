package model.supplier;

import model.item.Engine;
import model.warehouse.Warehouse;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EngineSupplier implements Runnable {

    private Warehouse<Engine> warehouse;
    private long timeout;
    private static AtomicInteger itemsSuppliedCounter = new AtomicInteger(0);
    private List<ItemsSuppliedCounterObserver> observers = new ArrayList<>();
    private final Object lock = new Object();

    private static final Logger log = Logger.getLogger(EngineSupplier.class);

    public EngineSupplier(Warehouse<Engine> warehouse, long timeout) {
        this.warehouse = warehouse;
        this.timeout = timeout;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        synchronized (lock) {
            this.timeout = timeout;
            lock.notify();
        }
    }

    private void incrementItemsSuppliedCounter() {
        int soldCars = itemsSuppliedCounter.incrementAndGet();
        notifyItemsSuppliedCounterObserver(soldCars);
    }

    public Thread getThread() {
        Thread thread = new Thread(this);
        thread.setName(this.toString() + "0");
        return thread;
    }

    public void addItemsSuppliedCounterObserver(ItemsSuppliedCounterObserver observer) {
        observers.add(observer);
    }

    private void notifyItemsSuppliedCounterObserver(int totalItemsSupplied) {
        for (ItemsSuppliedCounterObserver observer : observers) {
            observer.itemSupplied(totalItemsSupplied);
        }
    }

    @Override
    public void run() {
        try {
            while(true) {
                Engine engine = new Engine();
                warehouse.put(engine);
                incrementItemsSuppliedCounter();
                log.info(Thread.currentThread().getName() +
                        " : Accessory <" + engine.getId() + ">");
                synchronized (lock) {
                    if (timeout > 0) {
                        lock.wait(timeout);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("Supplying interrupted : ", e);
            System.exit(-1);
        }
    }

    public String toString() {
        return "Supplier<Engine>-";
    }

}
