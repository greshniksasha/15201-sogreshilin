import org.apache.log4j.Logger;

/**
 * Created by Alexander on 09/04/2017.
 */
public class Dealer {
    private CarWarehouse warehouse;
    private long timeout;

    private static final Logger log = Logger.getLogger(Dealer.class);

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public Dealer(CarWarehouse warehouse, long timeout) {
        this.warehouse = warehouse;
        this.timeout = timeout;
        Thread t = new Thread(new DealerRunnable());
        t.start();
    }

    class DealerRunnable implements Runnable {
        @Override
        public void run() {
            try {
                while(true) {
                    Car car = warehouse.get();
                    log.info("sold car #" + car.getId() +
                            " with body #" + car.getBody().getId() +
                            ", motor #" + car.getEngine().getId() +
                            ", accessory #" + car.getAccessory().getId());
                    Thread.sleep(timeout);
                }
            } catch (InterruptedException e) {
                log.error("Dealing interrupted : ", e);
                System.exit(-1);
            }

        }
    }
}

