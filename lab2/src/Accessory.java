/**
 * Created by Alexander on 06/04/2017.
 */
public class Accessory {
    public static final IdGenerator idGenerator = new IdGenerator();
    private long id;

    public Accessory() {
        this.id = idGenerator.createID();
    }

    public long getId() {
        return id;
    }
}
