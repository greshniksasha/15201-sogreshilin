import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Alexander on 06/04/2017.
 */
public class IdGenerator {
    private long idCounter = 0;

//    AtomicLong myAtomic;

    public synchronized long createID() {
        return idCounter++;
//        myAtomic.getAndIncrement();
    }
}
