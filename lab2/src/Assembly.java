import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import sun.jvm.hotspot.utilities.Assert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Alexander on 07/04/2017.
 */
public class Assembly {
    private Warehouse<Body> bodyWarehouse;
    private Warehouse<Engine> engineWarehouse;
    private Warehouse<Accessory> accessoryWarehouse;
    private CarWarehouse carWarehouse;
    private ThreadPool pool;

    public static final Logger log = Logger.getLogger(Assembly.class);

    public Assembly(Warehouse<Body> bodyWarehouse,
                    Warehouse<Engine> engineWarehouse,
                    Warehouse<Accessory> accessoryWarehouse,
                    CarWarehouse carWarehouse,
                    int workerCount,
                    int queueSize) {
        this.bodyWarehouse = bodyWarehouse;
        this.engineWarehouse = engineWarehouse;
        this.accessoryWarehouse = accessoryWarehouse;
        this.carWarehouse = carWarehouse;

        this.pool = new ThreadPool(workerCount, queueSize);
    }

    public void makeCar() {
        try {
            pool.addTask(new AssemblyRunnable());
        } catch (InterruptedException e) {
            log.error("Adding task to the pool exception : ", e);
            System.exit(-1);
        }
    }

    class AssemblyRunnable implements Runnable {
        @Override
        public void run() {
            try {
                Body body = bodyWarehouse.get();
                Engine engine = engineWarehouse.get();
                Accessory accessory = accessoryWarehouse.get();
                Car car = new Car(body, engine, accessory);
                carWarehouse.put(car);
                log.info("put  car #" + car.getId() +
                         " with body #" + car.getBody().getId() +
                         ", motor #" + car.getEngine().getId() +
                         ", accessory #" + car.getAccessory().getId());
            } catch (InterruptedException e) {
                log.error("Creating new car exception : ", e);
                System.exit(-1);
            }
        }
    }
}
