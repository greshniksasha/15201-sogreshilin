package model.item;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Alexander on 06/04/2017.
 */
public class Engine implements Item {
    public static final AtomicLong idGenerator = new AtomicLong(0);
    private long id;

    public Engine() {
        this.id = idGenerator.getAndIncrement();
    }

    public long getId() {
        return id;
    }
}
