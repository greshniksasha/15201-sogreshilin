import model.BlockingQueue;
import org.junit.Assert;
import org.junit.Test;

public class BlockingQueueTest {

    private final int CAPACITY = 1000;

    private BlockingQueue<Integer> queue = new BlockingQueue<>(CAPACITY);

    @Test
    public void enqueueAndDequeue() throws Exception {
        Thread producer = new Thread(() -> {
            for (int i = 0; i < CAPACITY * CAPACITY; ++i) {
                try {
                    queue.enqueue(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 0; i < CAPACITY * CAPACITY; ++i) {
                try {
                    int value = queue.dequeue();
                    Assert.assertEquals(i, value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        consumer.start();
        producer.start();
    }

    @Test
    public void getSizeAndCapacity() throws Exception {
        int size = CAPACITY / 2;
        for (int i = 0; i < size; ++i) {
            queue.enqueue(i);
        }
        Assert.assertEquals(size, queue.getSize());
        for (int i = 0; i < size; ++i) {
            queue.dequeue();
        }
        Assert.assertEquals(0, queue.getSize());
    }
}