/**
 * Created by Alexander on 06/04/2017.
 */
public class IdGenerator {
    private long idCounter = 0;

    public synchronized long createID() {
        return idCounter++;
    }
}
