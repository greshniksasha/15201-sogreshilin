package model.warehouse;

import model.Assembly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CarWarehouseController implements Runnable {
    private Assembly assembly;
    private CarWarehouse warehouse;
    private final Object lock = new Object();

    public static final Logger log = LogManager.getLogger(CarWarehouseController.class);

    public CarWarehouseController(CarWarehouse warehouse) {
        this.warehouse = warehouse;
    }

    public void setAssembly(Assembly assembly) {
        this.assembly = assembly;
    }

    public void notifyController() {
        synchronized (lock) {
            lock.notify();
        }
    }

    public Thread getThread() {
        Thread thread = new Thread(this);
        thread.setName("WarehouseController");
        return thread;
    }


    @Override
    public void run() {
        try {
            log.info("started");
            while (true) {
                synchronized (lock) {
                    int carsNeeded = warehouse.getCapacity() - warehouse.getSize() - assembly.getPoolSize();
                    for (int i = 0; i < carsNeeded; ++i) {
                        assembly.makeCar();
                    }
                    log.info("requested " + carsNeeded + " car" + (carsNeeded == 1 ? "" : "s"));
                    lock.wait();
                }
            }
        } catch (InterruptedException e) {
            log.info("stopped");
        }
    }


}
