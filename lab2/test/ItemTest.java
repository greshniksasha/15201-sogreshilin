import model.item.Accessory;
import model.item.Body;
import model.item.Car;
import model.item.Engine;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Alexander on 24/05/2017.
 */
public class ItemTest {

    private final int THREAD_COUNT = 100;
    private final int ITEM_COUNT = 100000 * THREAD_COUNT;

    @Test
    public void idGenerator() throws Exception {

        Integer[] accessories = new Integer[ITEM_COUNT];
        Integer[] engines = new Integer[ITEM_COUNT];
        Integer[] bodies = new Integer[ITEM_COUNT];
        Integer[] cars = new Integer[ITEM_COUNT];

        for (int i = 0; i < ITEM_COUNT; ++i) {
            accessories[i] = 0;
            engines[i] = 0;
            bodies[i] = 0;
            cars[i] = 0;
        }

        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0 ; i < THREAD_COUNT; ++i) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < ITEM_COUNT / THREAD_COUNT; ++j) {
                    Accessory acc = new Accessory();
                    Engine eng = new Engine();
                    Body bod = new Body();
                    Car car = new Car(bod, eng, acc);
                    accessories[(int)acc.getId()]++;
                    engines[(int)eng.getId()]++;
                    bodies[(int)bod.getId()]++;
                    cars[(int)car.getId()]++;
                }
            });
            threads[i].start();
            threads[i].join();
        }

        for (int i = 0; i < ITEM_COUNT; ++i) {
            Assert.assertEquals(1L, (long)accessories[i]);
            Assert.assertEquals(1L, (long)engines[i]);
            Assert.assertEquals(1L, (long)bodies[i]);
            Assert.assertEquals(1L, (long)cars[i]);
        }
        System.out.println("test passed");
    }
}