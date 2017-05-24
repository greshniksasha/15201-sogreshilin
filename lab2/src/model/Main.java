package model;

import model.item.Accessory;
import model.item.Body;
import model.item.Engine;
import model.contractor.AccessorySupplier;
import model.contractor.BodySupplier;
import model.contractor.Dealer;
import model.contractor.EngineSupplier;
import model.warehouse.CarWarehouse;
import model.warehouse.CarWarehouseController;
import model.warehouse.Warehouse;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
import view.FactoryForm;

public class Main {
    private static Warehouse<Engine> engineWarehouse;
    private static Warehouse<Body> bodyWarehouse;
    private static Warehouse<Accessory> accessoryWarehouse;
    private static CarWarehouse carWarehouse;

    private static EngineSupplier engineSupplier;
    private static BodySupplier bodySupplier;
    private static AccessorySupplier accessorySupplier;
    private static int accessorySupplierCount;
    private static Dealer dealer;
    private static int dealerCount;

    private static int threadPoolSize;
    private static int taskQueueSize;
    private static final Logger log = LogManager.getLogger(Main.class);

    private static void prepare() {
        Parser parser = new Parser("src/config.properties");
        engineWarehouse = new Warehouse<>(parser.engineWarehouseSize);
        bodyWarehouse = new Warehouse<>(parser.bodyWarehouseSize);
        accessoryWarehouse = new Warehouse<>(parser.accessoryWarehouseSize);
        bodySupplier = new BodySupplier(bodyWarehouse, parser.bodySupplierTimeout);
        engineSupplier = new EngineSupplier(engineWarehouse, parser.engineSupplierTimeout);
        carWarehouse = new CarWarehouse(parser.carWarehouseSize);
        threadPoolSize = parser.threadPoolSize;
        taskQueueSize = parser.taskQueueSize;
        accessorySupplier = new AccessorySupplier(accessoryWarehouse, parser.accessorySupplierTimeout);
        accessorySupplierCount = parser.accessorySupplierCount;
        dealer = new Dealer(carWarehouse, parser.dealerTimeout);
        dealerCount = parser.dealerCount;
        Boolean logSales = parser.logSales;
        if (!logSales) {

        }
    }

    public static void main(String[] args) {
        prepare();
        Assembly assembly = new Assembly(threadPoolSize, taskQueueSize);
        assembly.setAccessoryWarehouse(accessoryWarehouse);
        assembly.setBodyWarehouse(bodyWarehouse);
        assembly.setEngineWarehouse(engineWarehouse);
        assembly.setCarWarehouse(carWarehouse);

        CarWarehouseController carWarehouseController = new CarWarehouseController(carWarehouse);
        carWarehouseController.setAssembly(assembly);
        carWarehouse.setController(carWarehouseController);

        Factory factory = new Factory(bodySupplier, engineSupplier, accessorySupplier, accessorySupplierCount, assembly, dealer, dealerCount, carWarehouseController);
        FactoryForm form = new FactoryForm(factory);
        form.setVisible(true);
        log.info("set everything up, ready to start");
    }
}