package model.contractor;

import model.item.Car;
import model.warehouse.CarWarehouse;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Alexander on 09/04/2017.
 */
public class Dealer extends Contractor {

    private static AtomicInteger idGenerator = new AtomicInteger(0);
    private static AtomicInteger transactionCounter = new AtomicInteger(0);

    private CarWarehouse warehouse;
    private int timeout;
    private long id;

    private final Object lock;
    private static final Logger log = Logger.getLogger(Dealer.class);

    public void setTimeout(int timeout) {
        synchronized (lock) {
            this.timeout = timeout;
            lock.notify();
        }
    }

    public Dealer(CarWarehouse warehouse, int timeout) {
        this.warehouse = warehouse;
        this.timeout = timeout;
        this.id = idGenerator.getAndIncrement();
        this.lock = new Object();
    }

    public Thread getThread() {
        Thread thread = new Thread(new DealerRunnable());
        thread.setName("Dealer-" + id);
        return thread;
    }

    private void incrementSoldCarsCounter() {
        int soldCars = transactionCounter.incrementAndGet();
        notifyTransactionCounterObserver(soldCars);
    }


    public int getTimeout() {
        return timeout;
    }

    class DealerRunnable implements Runnable {
        @Override
        public void run() {
            try {
                while(true) {
                    Car car = warehouse.get();
                    incrementSoldCarsCounter();
                    log.info(Thread.currentThread().getName() +
                            " : Auto <" + car.getId() + ">" +
                            " (Body <" + car.getBody().getId() + ">" +
                            ", Motor <" + car.getEngine().getId() + ">" +
                            ", Accessory <" + car.getAccessory().getId() + ">)");
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
    }

}

