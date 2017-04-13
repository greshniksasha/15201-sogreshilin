/**
 * Created by Alexander on 09/04/2017.
 */
public class CarWarehouse {
    private BlockingQueue<Car> queue;
    private CarWarehouseController controller;

    public CarWarehouse(int size) {
        queue = new BlockingQueue<Car>(size);
    }

    public void setController(CarWarehouseController controller) {
        this.controller = controller;
    }

    public void put(Car car) throws InterruptedException {
        queue.enqueue(car);
    }

    public Car get() throws InterruptedException {
        Car car = queue.dequeue();
        controller.notifyController();
        return car;
    }

    public int placesLeft() {
        return queue.getSize() - queue.getElementsNo();
    }
}
