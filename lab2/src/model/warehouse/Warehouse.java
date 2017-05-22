package model.warehouse;

import model.BlockingQueue;

public class Warehouse<Type> {

    private BlockingQueue<Type> queue;

    public Warehouse(int size) {
        queue = new BlockingQueue<Type>(size);
    }

    public void put(Type element) throws InterruptedException {
        queue.enqueue(element);
    }

    public Type get() throws InterruptedException {
        Type element = queue.dequeue();
        return element;
    }

    public int getCapacity() {
        return queue.getCapacity();
    }

    public int getSize() {
        return queue.getSize();
    }

    public void addSizeObserver(BlockingQueue.SizeObserver observer) {
        queue.addSizeObserver(observer);
    }



}
