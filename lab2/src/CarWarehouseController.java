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

    public void control() {
        Thread thread = new Thread(new ControllerRunnable());
        thread.start();
    }

    class ControllerRunnable implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    synchronized (lock) {
                        for (int i = 0; i < warehouse.placesLeft(); ++i) {
                            assembly.makeCar();
                        }
                        lock.wait();
                    }
                }
            } catch (InterruptedException e) {
                log.error("Car warehouse controller interrupted : ", e);
                System.exit(-1);
            }
        }
    }

}
