package model;

import model.contractor.AccessorySupplier;
import model.contractor.BodySupplier;
import model.contractor.Dealer;
import model.contractor.EngineSupplier;
import model.warehouse.CarWarehouse;
import model.warehouse.CarWarehouseController;
import model.warehouse.Warehouse;

/**
 * Created by Alexander on 22/05/2017.
 */
public class Factory {
    private BodySupplier bodySupplier;
    private EngineSupplier engineSupplier;
    private AccessorySupplier accessorySupplier;
    private Assembly assembly;
    private Dealer dealer;

    private Thread bodySupplierThread;
    private Thread engineSupplierThread;
    private Thread carWarehouseControllerThread;
    private Thread[] accessorySupplierThreads;
    private Thread[] dealerThreads;

    private int accessorySupplierCount;
    private int dealerCount;

    public Factory(Configs configs) {
        assembly = new Assembly(configs.getThreadPoolSize(), configs.getTaskQueueSize());
        assembly.setAccessoryWarehouse(new Warehouse<>(configs.getAccessoryWarehouseSize()));
        assembly.setBodyWarehouse(new Warehouse<>(configs.getBodyWarehouseSize()));
        assembly.setEngineWarehouse(new Warehouse<>(configs.getEngineWarehouseSize()));
        assembly.setCarWarehouse(new CarWarehouse(configs.getCarWarehouseSize()));

        CarWarehouseController carWarehouseController = new CarWarehouseController(assembly.getCarWarehouse());
        carWarehouseController.setAssembly(assembly);
        assembly.getCarWarehouse().setController(carWarehouseController);

        dealer = new Dealer(assembly.getCarWarehouse(), configs.getDealerTimeout());
        bodySupplier = new BodySupplier(assembly.getBodyWarehouse(), configs.getBodySupplierTimeout());
        engineSupplier = new EngineSupplier(assembly.getEngineWarehouse(), configs.getEngineSupplierTimeout());
        accessorySupplier = new AccessorySupplier(assembly.getAccessoryWarehouse(), configs.getAccessorySupplierTimeout());

        bodySupplierThread = bodySupplier.getThread();
        engineSupplierThread = engineSupplier.getThread();
        carWarehouseControllerThread = carWarehouseController.getThread();

        accessorySupplierCount = configs.getAccessorySupplierCount();
        accessorySupplierThreads = new Thread[accessorySupplierCount];
        for (int i = 0; i < accessorySupplierCount; ++i) {
            accessorySupplierThreads[i] = accessorySupplier.getThread();
        }

        dealerCount = configs.getDealerCount();
        dealerThreads = new Thread[dealerCount];
        for (int i = 0; i < dealerCount; ++i) {
            dealerThreads[i] = dealer.getThread();
        }

    }

    public void start() {
        bodySupplierThread.start();
        engineSupplierThread.start();
        for (Thread thread : accessorySupplierThreads) {
            thread.start();
        }
        for (Thread thread : assembly.getPool().getThreads()) {
            thread.start();
        }
        carWarehouseControllerThread.start();
        for (Thread thread : dealerThreads) {
            thread.start();
        }
    }

    public void finish() {
        bodySupplierThread.interrupt();
        engineSupplierThread.interrupt();
        carWarehouseControllerThread.interrupt();
        for (Thread thread : accessorySupplierThreads) {
            thread.interrupt();
        }
        for (Thread thread : assembly.getPool().getThreads()) {
            thread.interrupt();
        }
        for (Thread thread : dealerThreads) {
            thread.interrupt();
        }
    }

    public int getAccessorySupplierCount() {
        return accessorySupplierCount;
    }

    public int getDealerCount() {
        return dealerCount;
    }

    public Assembly getAssembly() {
        return assembly;
    }

    public BodySupplier getBodySupplier() {
        return bodySupplier;
    }

    public EngineSupplier getEngineSupplier() {
        return engineSupplier;
    }

    public AccessorySupplier getAccessorySupplier() {
        return accessorySupplier;
    }

    public Dealer getDealer() {
        return dealer;
    }
}
