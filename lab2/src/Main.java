/**
 * Created by Alexander on 13/04/2017.
 */
public class Main {

    private static Warehouse<Engine> engineWarehouse;
    private static Warehouse<Body> bodyWarehouse;
    private static Warehouse<Accessory> accessoryWarehouse;
    private static Supplier<Engine> engineSupplier;
    private static Supplier<Body> bodySupplier;
    private static Supplier[] accessorySuppliers;
    private static CarWarehouse carWarehouse;
    private static Dealer[] dealers;
    private static int workerCount;
    private static int taskQueueSize;

    private static void prepare() {
        Parser parser = new Parser("src/config.properties");
        engineWarehouse = new Warehouse<>(parser.engineWarehouseSize);
        bodyWarehouse = new Warehouse<>(parser.bodyWarehouseSize);
        accessoryWarehouse = new Warehouse<>(parser.accessoryWarehouseSize);
        engineSupplier = new Supplier<>(Engine.class, engineWarehouse, parser.engineSupplierTimeout);
        bodySupplier = new Supplier<>(Body.class, bodyWarehouse, parser.bodySupplierTimeout);
        carWarehouse = new CarWarehouse(parser.carWarehouseSize);
        workerCount = parser.workerCount;
        taskQueueSize = parser.taskQueueSize;
        accessorySuppliers =  new Supplier[parser.accessorySupplierCount];
        for (int i = 0; i < parser.accessorySupplierCount; ++i) {
            accessorySuppliers[i] = new Supplier(Accessory.class, accessoryWarehouse, parser.accessorySupplierTimeout);
        }
        dealers = new Dealer[parser.dealerCount];
        for (int i = 0; i < parser.dealerCount; ++i) {
            dealers[i] = new Dealer(carWarehouse, parser.dealerTimeout);
        }
    }

    public static void main(String[] args) {
        prepare();
        Assembly assembly = new Assembly(bodyWarehouse, engineWarehouse, accessoryWarehouse, carWarehouse, workerCount, taskQueueSize);
        CarWarehouseController controller = new CarWarehouseController(carWarehouse);
        controller.setAssembly(assembly);
        carWarehouse.setController(controller);

        controller.control();

    }
}
