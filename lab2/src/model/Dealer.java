package model;

import model.item.Car;
import model.warehouse.CarWarehouse;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Alexander on 09/04/2017.
 */
public class Dealer {

    private static AtomicInteger idGenerator = new AtomicInteger(0);
    private static AtomicInteger soldCarsCounter = new AtomicInteger(0);

    private CarWarehouse warehouse;
    private long timeout;
    private long id;

    private final Object lock;
    private List<SoldCarsCounterObserver> observers = new ArrayList<>();
    private static final Logger log = Logger.getLogger(Dealer.class);

    public void setTimeout(long timeout) {
        synchronized (lock) {
            this.timeout = timeout;
            lock.notify();
        }
    }

    public Dealer(CarWarehouse warehouse, long timeout) {
        this.warehouse = warehouse;
        this.timeout = timeout;
        this.id = idGenerator.getAndIncrement();
        this.lock = new Object();
    }

    public Thread getThread() {
        Thread thread = new Thread(new DealerRunnable());
        thread.setName("model.Dealer-" + id);
        return thread;
    }

    private void incrementSoldCarsCounter() {
        int soldCars = soldCarsCounter.incrementAndGet();
        notifySoldCarsCounterObserver(soldCars);
    }


    public long getTimeout() {
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
                            " (model.item.Body <" + car.getBody().getId() + ">" +
                            ", Motor <" + car.getEngine().getId() + ">" +
                            ", model.item.Accessory <" + car.getAccessory().getId() + ">)");
                    synchronized (lock) {
                        if (timeout > 0) {
                            lock.wait(timeout);
                        }
                    }
                }
            } catch (InterruptedException e) {
                log.error("Dealing interrupted : ", e);
                System.exit(0);
            }

        }
    }

    public void addSoldCarsCounterObserver(SoldCarsCounterObserver observer) {
        observers.add(observer);
    }

    private void notifySoldCarsCounterObserver(int count) {
        for (SoldCarsCounterObserver observer : observers) {
            observer.carSold(count);
        }
    }

    public interface SoldCarsCounterObserver {
        void carSold(int count);
    }

}

