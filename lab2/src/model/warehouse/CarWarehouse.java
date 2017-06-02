package model.warehouse;

import model.item.Car;

public class CarWarehouse extends Warehouse<Car> {
    private CarWarehouseController controller;

    public CarWarehouse(int size) {
        super(size);
    }

    public void setController(CarWarehouseController controller) {
        this.controller = controller;
    }

    public Car get() throws InterruptedException {
        Car car = super.get();
        controller.notifyController();
        return car;
    }
}
