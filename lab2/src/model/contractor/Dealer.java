package model.contractor;

import model.item.Car;
import model.warehouse.CarWarehouse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.concurrent.atomic.AtomicInteger;

public class Dealer extends Contractor implements Runnable {

    private static AtomicInteger idGenerator = new AtomicInteger(0);
    private static AtomicInteger transactionCounter = new AtomicInteger(0);

    private CarWarehouse warehouse;
    private int timeout;

    private final Object lock;
    private static final Logger log = LogManager.getLogger(Dealer.class);
    private static final Marker CAR_SALES_MARKER = MarkerManager.getMarker("CAR_SALES_MARKER");

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
            while(!Thread.interrupted()) {
                Car car = warehouse.get();
                incrementSoldCarsCounter();
                log.info("got item C<{}>(B<{}>,E<{}>,A<{}>) from warehouse",
                        car.getId(),
                        car.getBody().getId(),
                        car.getEngine().getId(),
                        car.getAccessory().getId());
                waitSettingTimeout(timeout);
            }
        } catch (InterruptedException e) {
            log.info("stopped");
        }


    }

}

