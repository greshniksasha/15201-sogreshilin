package model.item;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Alexander on 06/04/2017.
 */
public class Body implements Item {
    public static final AtomicLong idGenerator = new AtomicLong(0);
    private long id;

    public long getId() {
        return id;
    }

    public Body() {
        this.id = idGenerator.getAndIncrement();
    }
}
