import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Alexander on 09/04/2017.
 */
public class Dealer implements Observable {

    private static IdGenerator idGenerator = new IdGenerator();
    private static int soldCounter = 0;

    private CarWarehouse warehouse;
    private long timeout;
    private long id;

    private final Object lock;
    private List<Observer> observers;
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
        this.id = idGenerator.createID();
        this.observers = new LinkedList<>();
        this.lock = new Object();

    }

    public Thread getThread() {
        Thread thread = new Thread(new DealerRunnable());
        thread.setName("Dealer-" + id);
        return thread;
    }

    private synchronized void incrementSoldCounter() {
        ++soldCounter;
        notifyObservers();
    }

    public int getSoldCounter() {
        return soldCounter;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.setChanges();
        }
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
                    incrementSoldCounter();
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
                log.error("Dealing interrupted : ", e);
                System.exit(0);
            }

        }
    }
}

