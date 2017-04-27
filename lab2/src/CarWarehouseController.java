import org.apache.log4j.Logger;

/**
 * Created by Alexander on 07/04/2017.
 */
public class CarWarehouseController {
    private Assembly assembly;
    private CarWarehouse warehouse;
    private final Object lock = new Object();

    public static final Logger log = Logger.getLogger(CarWarehouseController.class);

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
        Thread thread = new Thread(new ControllerRunnable());
        thread.setName("CarWarehouseController");
        return thread;
    }

    class ControllerRunnable implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    synchronized (lock) {
                        int carsNeeded = warehouse.getCapacity() - warehouse.getSize() - assembly.getWorkerCount();
                        for (int i = 0; i < carsNeeded; ++i) {
                            assembly.makeCar();
                        }
                        lock.wait();
                    }
                }
            } catch (InterruptedException e) {
                log.error("Car warehouse controller interrupted : ", e);
                System.exit(0);
            }
        }
    }

}
