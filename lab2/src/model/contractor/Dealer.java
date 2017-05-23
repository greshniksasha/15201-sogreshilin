package model.contractor;

import model.item.Car;
import model.warehouse.CarWarehouse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public class Dealer extends Contractor implements Runnable {

    private static AtomicInteger idGenerator = new AtomicInteger(0);
    private static AtomicInteger transactionCounter = new AtomicInteger(0);

    private CarWarehouse warehouse;
    private int timeout;

    private final Object lock;
    private static final Logger log = LogManager.getLogger(Dealer.class);

    public void setTimeout(int timeout) {
        synchronized (lock) {
            this.timeout = timeout;
            lock.notify();
        }
        log.info("timeout changed");
    }

    public Dealer(CarWarehouse warehouse, int timeout) {
        this.warehouse = warehouse;
        this.timeout = timeout;
        this.lock = new Object();
    }

    public Thread getThread() {
        Thread thread = new Thread(this);
        thread.setName("Dealer[" + idGenerator.getAndIncrement() + "]");
        return thread;
    }

    private void incrementSoldCarsCounter() {
        int soldCars = transactionCounter.incrementAndGet();
        notifyTransactionCounterObserver(soldCars);
    }


    public int getTimeout() {
        return timeout;
    }

    @Override
    public void run() {
        try {
            log.info("started");
            while(true) {
                Car car = warehouse.get();
                incrementSoldCarsCounter();
                log.info("got item C<" + car.getId() + ">" +
                        "(B<" + car.getBody().getId() + ">" +
                        ",E<" + car.getEngine().getId() + ">" +
                        "A<" + car.getAccessory().getId() + ">) from warehouse");
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

}

