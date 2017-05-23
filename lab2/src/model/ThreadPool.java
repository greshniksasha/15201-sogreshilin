package model;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;


public class ThreadPool {

    private BlockingQueue<Runnable> queue;
    private Thread [] pool;
    private static AtomicInteger threadId = new AtomicInteger(0);
    private static final Logger log = LogManager.getLogger(ThreadPool.class);

    public ThreadPool(int threadCount, int queueSize) {
        pool = new Thread[threadCount];
        queue = new BlockingQueue<>(queueSize);
        for (int i = 0; i < threadCount; ++i) {
            ThreadPoolRunnable runnable = new ThreadPoolRunnable();
            pool[i] = new Thread(runnable);
            pool[i].setName("PoolWorker[" + threadId.getAndIncrement() + "]");
        }
    }

    public Thread[] getThreads() {
        return pool;
    }

    public void addTask(Runnable task) throws InterruptedException {
        queue.enqueue(task);
    }

    public int getSize() {
        return queue.getSize();
    }

    class ThreadPoolRunnable implements Runnable {
        @Override
        public void run() {
            log.info("started");
            try{
                while(!Thread.interrupted()) {
                    Runnable runnable = queue.dequeue();
                    runnable.run();
                }
            } catch (InterruptedException e) {
                log.trace("stopped");
            }
        }
    }

}
