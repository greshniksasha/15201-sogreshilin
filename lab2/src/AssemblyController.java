/**
 * Created by Alexander on 27/04/2017.
 */
public class AssemblyController {

    private Assembly assembly;
    private Supplier<Body> bodySupplier;
    private Supplier<Engine> engineSupplier;
    private AccessorySuppliers accessorySuppliers;
    private Dealer[] dealers;
    private CarWarehouseController carWarehouseController;

    private Thread bodySupplierThread;
    private Thread engineSupplerThread;
    private Thread carWarehouseControllerThread;
    private Thread[] accessorySuppliersThreads;
    private Thread[] dealersThreads;

    public AssemblyController(Assembly assembly,
                              Supplier<Body> bodySupplier,
                              Supplier<Engine> engineSupplier,
                              AccessorySuppliers accessorySuppliers,
                              Dealer[] dealers,
                              CarWarehouseController carWarehouseController) {
        this.assembly = assembly;
        this.bodySupplier = bodySupplier;
        this.engineSupplier = engineSupplier;
        this.accessorySuppliers = accessorySuppliers;
        this.dealers = dealers;
        this.carWarehouseController = carWarehouseController;
        this.bodySupplierThread = bodySupplier.getThread();
        this.engineSupplerThread = engineSupplier.getThread();
        this.carWarehouseControllerThread = carWarehouseController.getThread();
        this.accessorySuppliersThreads = new Thread[accessorySuppliers.length()];
        for (int i = 0; i < accessorySuppliers.length(); ++i) {
            accessorySuppliersThreads[i] = accessorySuppliers.getSuppliers()[i].getThread();
        }
        this.dealersThreads = new Thread[dealers.length];
        for (int i = 0; i < dealers.length; ++i) {
            dealersThreads[i] = dealers[i].getThread();
        }
    }

    public void start() {
        bodySupplierThread.start();
        engineSupplerThread.start();
        carWarehouseControllerThread.start();
        for (Thread thread : accessorySuppliersThreads) {
            thread.start();
        }
        for (Thread thread : dealersThreads) {
            thread.start();
        }
    }

    public void finish() {
        bodySupplierThread.interrupt();
        engineSupplerThread.interrupt();
        carWarehouseControllerThread.interrupt();
        for (Thread thread : accessorySuppliersThreads) {
            thread.interrupt();
        }
        for (Thread thread : dealersThreads) {
            thread.interrupt();
        }
    }

    public Assembly getAssembly() {
        return assembly;
    }

    public Supplier<Body> getBodySupplier() {
        return bodySupplier;
    }

    public Supplier<Engine> getEngineSupplier() {
        return engineSupplier;
    }

    public AccessorySuppliers getAccessorySuppliers() {
        return accessorySuppliers;
    }

    public Dealer[] getDealers() {
        return dealers;
    }

    public CarWarehouseController getCarWarehouseController() {
        return carWarehouseController;
    }
}
