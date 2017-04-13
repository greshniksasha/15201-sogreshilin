import org.apache.log4j.Logger;

/**
 * Created by Alexander on 06/04/2017.
 */
public class ThreadPool {

    private BlockingQueue<Runnable> queue;
    private Thread [] pool;
    private static final Logger log = Logger.getLogger(Supplier.class);

    public ThreadPool(int threadCount, int queueSize) {
        pool = new Thread[threadCount];
        queue = new BlockingQueue<>(queueSize);
        for (int i = 0; i < threadCount; ++i) {
            ThreadPoolRunnable runnable = new ThreadPoolRunnable();
            pool[i] = new Thread(runnable);
            pool[i].start();
        }
    }

    public void addTask(Runnable task) throws InterruptedException {
        queue.enqueue(task);
    }

    class ThreadPoolRunnable implements Runnable {
        @Override
        public void run() {
            try{
                while(!Thread.interrupted()) {
                    Runnable runnable = queue.dequeue();
                    runnable.run();
                }
            } catch (InterruptedException e) {
                log.error("Thread pool interrupted : ", e);
                System.exit(-1);
            }
        }
    }

}
