/**
 * Created by Alexander on 06/04/2017.
 */
public class Body {
    public static final IdGenerator idGenerator = new IdGenerator();
    private long id;

    public long getId() {
        return id;
    }

    public Body() {
        this.id = idGenerator.createID();
    }
}
