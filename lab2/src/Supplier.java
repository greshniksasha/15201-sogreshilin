/**
 * Created by Alexander on 07/04/2017.
 */

import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;


public class Supplier<Type extends Item> implements Observable {

    private Class<Type> elementClass;
    private Warehouse<Type> warehouse;
    private final Object lock;
    private long timeout;
    private long id;

    private List<Observer> observers;
    private static IdGenerator idGenerator = new IdGenerator();
    private static final Logger log = Logger.getLogger(Supplier.class);

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        synchronized (lock) {
            this.timeout = timeout;
            lock.notify();
        }
    }

    public Supplier(Class<Type> elementClass, Warehouse<Type> warehouse, long timeout) {
        this.elementClass = elementClass;
        this.warehouse = warehouse;
        this.timeout = timeout;
        this.lock = new Object();
        this.id = idGenerator.createID();
        this.observers = new LinkedList<>();
    }

    public Thread getThread() {
        Thread thread = new Thread(new SupplierRunnable());
        thread.setName(this.toString() + id);
        return thread;
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

    class SupplierRunnable implements Runnable {
        @Override
        public void run() {
            try {
                    while(true) {
                        Type element = elementClass.newInstance();
                        warehouse.put(element);
//                        log.info(Thread.currentThread().getName() +
//                                " : " + elementClass.getSimpleName() +
//                                " <" + element.getId() + ">");
                        notifyObservers();
                        synchronized (lock) {
                            if (timeout > 0) {
                                lock.wait(timeout);
                            }
                        }
                    }
            } catch (InterruptedException e) {
                log.error("Supplying interrupted : ", e);
                System.exit(-1);
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("Supplier object instance exception : ", e);
                System.exit(-1);
            }
        }
    }

    @Override
    public String toString() {
        return "Supplier<"+elementClass.getSimpleName() + ">-";
    }
}
