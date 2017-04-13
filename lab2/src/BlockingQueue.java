import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by Alexander on 06/04/2017.
 */
public class BlockingQueue<Type> {

    private final int size;
    private Queue<Type> queue;
    private Object lock = new Object();

    public int getSize() {
        return size;
    }

    public BlockingQueue(int size) {
        this.size = size;
        queue =  new LinkedList<>();
    }

    public int getElementsNo() {
        return queue.size();
    }

    public void enqueue(Type element) throws InterruptedException {
        synchronized (lock) {
            while (queue.size() == size) {
                lock.wait();
            }
            queue.add(element);
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
            lock.notifyAll();
        }
        return element;
    }


}
