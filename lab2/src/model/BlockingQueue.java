package model;

import model.warehouse.Warehouse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BlockingQueue<Type> {

    private final int capacity;
    private Queue<Type> queue;
    private final Object lock = new Object();
    private List<SizeObserver> observers = new ArrayList<>();

    public BlockingQueue(int size) {
        this.capacity = size;
        queue =  new LinkedList<>();
    }

    public int getCapacity() {
        return capacity;
    }

    public int getSize() {
        return queue.size();
    }

    public void enqueue(Type element) throws InterruptedException {
        synchronized (lock) {
            while (queue.size() == capacity) {
                lock.wait();
            }
            queue.add(element);
            sizeChanged(queue.size());
            lock.notifyAll();
        }
    }

    public Type dequeue() throws InterruptedException {
        Type element = null;
        synchronized (lock) {
            while (queue.size() == 0) {
                lock.wait();
            }
            element = queue.remove();
            sizeChanged(queue.size());
            lock.notifyAll();
        }
        return element;
    }

    public void addSizeObserver(SizeObserver observer) {
        observers.add(observer);
    }

    private void sizeChanged(int size) {
        for (SizeObserver observer : observers) {
            observer.sizeChanged(size);
        }
    }

    public interface SizeObserver {
        void sizeChanged(int size);
    }

}
