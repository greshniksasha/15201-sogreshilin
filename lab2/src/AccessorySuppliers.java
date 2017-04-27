import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alexander on 27/04/2017.
 */
public class AccessorySuppliers implements Observable {

    private Supplier[] suppliers;
    private long timeout;

    private List<Observer> observers;

    public long getTimeout() {
        return timeout;
    }

    public int length() {
        return suppliers.length;
    }

    public void setTimeout(long timeout) {
        for (Supplier<Accessory> supplier : suppliers) {
            supplier.setTimeout(timeout);
        }
        this.timeout = timeout;
        this.notifyObservers();
    }

    public AccessorySuppliers(int count, Warehouse<Accessory> warehouse, long timeout) {
        this.suppliers =  new Supplier[count];
        this.timeout = timeout;
        this.observers = new LinkedList<>();
        for (int i = 0; i < count; ++i) {
            suppliers[i] = new Supplier(Accessory.class, warehouse, timeout);
        }
    }

    public Supplier[] getSuppliers() {
        return suppliers;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.setChanges();
        }
    }
}
