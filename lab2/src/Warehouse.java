import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alexander on 06/04/2017.
 */
public class Warehouse<Type> implements Observable {
    private int itemsCount;
    private BlockingQueue<Type> queue;
    private List<Observer> observers;

    public Warehouse(int size) {
        itemsCount = 0;
        queue = new BlockingQueue<Type>(size);
        observers = new LinkedList<>();
    }

    public int getItemsCount() {
        return itemsCount;
    }

    public void put(Type element) throws InterruptedException {
        ++itemsCount;
        queue.enqueue(element);
        notifyObservers();
    }

    public Type get() throws InterruptedException {
        Type element = queue.dequeue();
        notifyObservers();
        return element;
    }

    public int getCapacity() {
        return queue.getCapacity();
    }

    public int getSize() {
        return queue.getSize();
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
