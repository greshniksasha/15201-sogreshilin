package model;

import model.item.Accessory;
import model.item.Body;
import model.item.Car;
import model.item.Engine;
import model.warehouse.CarWarehouse;
import model.warehouse.Warehouse;
import org.apache.log4j.Logger;

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

    public Assembly(int workerCount, int queueSize) {
        this.pool = new ThreadPool(workerCount, queueSize);
    }

    public void setBodyWarehouse(Warehouse<Body> bodyWarehouse) {
        this.bodyWarehouse = bodyWarehouse;
    }

    public void setEngineWarehouse(Warehouse<Engine> engineWarehouse) {
        this.engineWarehouse = engineWarehouse;
    }

    public void setAccessoryWarehouse(Warehouse<Accessory> accessoryWarehouse) {
        this.accessoryWarehouse = accessoryWarehouse;
    }

    public void setCarWarehouse(CarWarehouse carWarehouse) {
        this.carWarehouse = carWarehouse;
    }

    public Warehouse<Body> getBodyWarehouse() {
        return bodyWarehouse;
    }

    public Warehouse<Engine> getEngineWarehouse() {
        return engineWarehouse;
    }

    public Warehouse<Accessory> getAccessoryWarehouse() {
        return accessoryWarehouse;
    }

    public CarWarehouse getCarWarehouse() {
        return carWarehouse;
    }

    public ThreadPool getPool() {
        return pool;
    }

    public int getPoolSize() {
        return pool.getSize();
    }

    public void makeCar() {
        try {
            pool.addTask(new MakeCar());
        } catch (InterruptedException e) {
            log.error("Adding task to the pool exception : ", e);
            System.exit(-1);
        }
    }

    class MakeCar implements Runnable {
        @Override
        public void run() {
            try {
                Body body = bodyWarehouse.get();
                Engine engine = engineWarehouse.get();
                Accessory accessory = accessoryWarehouse.get();
                Car car = new Car(body, engine, accessory);
                carWarehouse.put(car);
//                log.info("put  car #" + car.getId() +
//                         " with body #" + car.getBody().getId() +
//                         ", motor #" + car.getEngine().getId() +
//                         ", accessory #" + car.getAccessory().getId());
            } catch (InterruptedException e) {
                log.error("Creating new car exception : ", e);
                System.exit(-1);
            }
        }
    }
}
