import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Created by Alexander on 13/04/2017.
 */
public class Main {
    private static Warehouse<Engine> engineWarehouse;
    private static Warehouse<Body> bodyWarehouse;
    private static Warehouse<Accessory> accessoryWarehouse;
    private static Supplier<Engine> engineSupplier;
    private static Supplier<Body> bodySupplier;
    private static AccessorySuppliers accessorySuppliers;
    private static CarWarehouse carWarehouse;
    private static Dealer[] dealers;
    private static int workerCount;
    private static int taskQueueSize;
    private static Boolean logSales;

    private static void prepare() {
        Parser parser = new Parser("src/config.properties");
        engineWarehouse = new Warehouse<>(parser.engineWarehouseSize);
        bodyWarehouse = new Warehouse<>(parser.bodyWarehouseSize);
        accessoryWarehouse = new Warehouse<>(parser.accessoryWarehouseSize);
        bodySupplier = new Supplier<>(Body.class, bodyWarehouse, parser.bodySupplierTimeout);
        engineSupplier = new Supplier<>(Engine.class, engineWarehouse, parser.engineSupplierTimeout);
        carWarehouse = new CarWarehouse(parser.carWarehouseSize);
        workerCount = parser.workerCount;
        taskQueueSize = parser.taskQueueSize;
        accessorySuppliers = new AccessorySuppliers(parser.accessorySupplierCount, accessoryWarehouse, parser.accessorySupplierTimeout);
        dealers = new Dealer[parser.dealerCount];
        for (int i = 0; i < parser.dealerCount; ++i) {
            dealers[i] = new Dealer(carWarehouse, parser.dealerTimeout);
        }
        logSales = parser.logSales;
        if (!logSales) {
            Logger.getRootLogger().setLevel(Level.OFF);
        }
    }

    public static void main(String[] args) {
        prepare();
        Assembly assembly = new Assembly(bodyWarehouse, engineWarehouse, accessoryWarehouse, carWarehouse, workerCount, taskQueueSize);
        CarWarehouseController carWarehouseController = new CarWarehouseController(carWarehouse);
        carWarehouseController.setAssembly(assembly);
        carWarehouse.setController(carWarehouseController);
        AssemblyController assemblyController = new AssemblyController(assembly, bodySupplier, engineSupplier, accessorySuppliers, dealers, carWarehouseController);
        FactoryForm form = new FactoryForm(assemblyController, logSales);
    }
}