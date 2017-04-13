/**
 * Created by Alexander on 07/04/2017.
 */
public class Car {
    private static final IdGenerator idGenerator = new IdGenerator();
    private Body body;
    private Engine engine;
    private Accessory accessory;
    private long id;

    public long getId() {
        return id;
    }

    public Body getBody() {
        return body;
    }

    public Engine getEngine() {
        return engine;
    }

    public Accessory getAccessory() {
        return accessory;
    }

    public Car(Body body, Engine engine, Accessory accessory) {
        this.body = body;
        this.engine = engine;
        this.accessory = accessory;
        this.id = idGenerator.createID();
    }
}
