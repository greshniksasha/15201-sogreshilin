package model;

import model.item.Body;
import model.item.Engine;
import model.supplier.AccessorySupplier;
import model.supplier.BodySupplier;
import model.supplier.EngineSupplier;
import model.warehouse.CarWarehouseController;

/**
 * Created by Alexander on 22/05/2017.
 */
public class Factory {
    private BodySupplier bodySupplier;
    private EngineSupplier engineSupplier;
    private AccessorySupplier accessorySupplier;
    private Assembly assembly;
    private Dealer dealer;
    private CarWarehouseController carWarehouseController;

    private Thread bodySupplierThread;
    private Thread engineSupplerThread;
    private Thread carWarehouseControllerThread;
    private Thread[] accessorySupplierThreads;
    private Thread[] dealerThreads;

    public Factory(BodySupplier bodySupplier,
                   EngineSupplier engineSupplier,
                   AccessorySupplier accessorySupplier,
                   int accessorySupplierCount,
                   Assembly assembly,
                   Dealer dealer,
                   int dealerCount,
                   CarWarehouseController carWarehouseController) {
        this.bodySupplier = bodySupplier;
        this.engineSupplier = engineSupplier;
        this.accessorySupplier = accessorySupplier;
        this.assembly = assembly;
        this.dealer = dealer;
        this.carWarehouseController = carWarehouseController;

        this.bodySupplierThread = bodySupplier.getThread();
        this.engineSupplerThread = engineSupplier.getThread();
        this.carWarehouseControllerThread = carWarehouseController.getThread();
        this.accessorySupplierThreads = new Thread[accessorySupplierCount];
        for (int i = 0; i < accessorySupplierCount; ++i) {
            accessorySupplierThreads[i] = accessorySupplier.getThread();
        }
        this.dealerThreads = new Thread[dealerCount];
        for (int i = 0; i < dealerCount; ++i) {
            dealerThreads[i] = dealer.getThread();
        }
    }

    public void start() {
        bodySupplierThread.start();
        engineSupplerThread.start();
        carWarehouseControllerThread.start();
        for (Thread thread : accessorySupplierThreads) {
            thread.start();
        }
        for (Thread thread : dealerThreads) {
            thread.start();
        }
    }

    public void finish() {
        bodySupplierThread.interrupt();
        engineSupplerThread.interrupt();
        carWarehouseControllerThread.interrupt();
        for (Thread thread : accessorySupplierThreads) {
            thread.interrupt();
        }
        for (Thread thread : dealerThreads) {
            thread.interrupt();
        }
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

    public CarWarehouseController getCarWarehouseController() {
        return carWarehouseController;
    }
}
