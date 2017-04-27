/**
 * Created by Alexander on 06/04/2017.
 */
public class Engine implements Item {
    public static final IdGenerator idGenerator = new IdGenerator();
    private long id;

    public Engine() {
        this.id = idGenerator.createID();
    }

    public long getId() {
        return id;
    }
}
