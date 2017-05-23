package model;

import model.contractor.AccessorySupplier;
import model.contractor.BodySupplier;
import model.contractor.Dealer;
import model.contractor.EngineSupplier;
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

    private Thread bodySupplierThread;
    private Thread engineSupplierThread;
    private Thread carWarehouseControllerThread;
    private Thread[] accessorySupplierThreads;
    private Thread[] dealerThreads;

    private int accessorySupplierCount;
    private int dealerCount;

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

        this.bodySupplierThread = bodySupplier.getThread();
        this.engineSupplierThread = engineSupplier.getThread();
        this.carWarehouseControllerThread = carWarehouseController.getThread();
        this.accessorySupplierThreads = new Thread[accessorySupplierCount];
        for (int i = 0; i < accessorySupplierCount; ++i) {
            accessorySupplierThreads[i] = accessorySupplier.getThread();
        }
        this.dealerThreads = new Thread[dealerCount];
        for (int i = 0; i < dealerCount; ++i) {
            dealerThreads[i] = dealer.getThread();
        }

        this.dealerCount = dealerCount;
        this.accessorySupplierCount = accessorySupplierCount;
    }

    public void start() {
        bodySupplierThread.start();
        engineSupplierThread.start();
        carWarehouseControllerThread.start();
        for (Thread thread : accessorySupplierThreads) {
            thread.start();
        }
        for (Thread thread : dealerThreads) {
            thread.start();
        }
        for (Thread thread : assembly.getPool().getThreads()) {
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
        for (Thread thread : dealerThreads) {
            thread.interrupt();
        }
        for (Thread thread : assembly.getPool().getThreads()) {
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
