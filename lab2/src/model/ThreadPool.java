package model;

import org.apache.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Alexander on 06/04/2017.
 */
public class ThreadPool {

    private BlockingQueue<Runnable> queue;
    private Thread [] pool;
    private static AtomicInteger threadId = new AtomicInteger(0);
    private static final Logger log = Logger.getLogger(ThreadPool.class);

    public ThreadPool(int threadCount, int queueSize) {
        pool = new Thread[threadCount];
        queue = new BlockingQueue<>(queueSize);
        for (int i = 0; i < threadCount; ++i) {
            ThreadPoolRunnable runnable = new ThreadPoolRunnable();
            pool[i] = new Thread(runnable);
            pool[i].setName("PoolThread-" + threadId.getAndIncrement());
            pool[i].start();
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
            try{
                while(!Thread.interrupted()) {
                    Runnable runnable = queue.dequeue();
                    runnable.run();
                }
            } catch (InterruptedException e) {
                log.trace(Thread.currentThread().getName() + " stopped");
                return;
            }
        }
    }

}
