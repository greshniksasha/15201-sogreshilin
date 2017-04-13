/**
 * Created by Alexander on 06/04/2017.
 */
public class Warehouse<Type> {
    private BlockingQueue<Type> queue;

    public Warehouse(int size) {
        queue = new BlockingQueue<Type>(size);
    }

    public void put(Type element) throws InterruptedException {
        queue.enqueue(element);
    }

    public Type get() throws InterruptedException {
        return queue.dequeue();
    }

    public boolean isFull() {
        return queue.getElementsNo() == queue.getSize();
    }

    public int getSize() {
        return queue.getSize();
    }

    public int getElementsNo() {
        return queue.getElementsNo();
    }
}
