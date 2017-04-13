/**
 * Created by Alexander on 07/04/2017.
 */

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class Supplier<Type> {

    private Class<Type> elementClass;
    private Warehouse<Type> warehouse;
    private long timeout;
    private static final Logger log = Logger.getLogger(Supplier.class);

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public Supplier(Class<Type> elementClass, Warehouse<Type> warehouse, long timeout) {
        this.elementClass = elementClass;
        this.warehouse = warehouse;
        this.timeout = timeout;
        new Thread(new SupplierRunnable()).start();
    }

    class SupplierRunnable implements Runnable {
        @Override
        public void run() {
            try {
                while(true) {
                    Type element = elementClass.newInstance();
                    warehouse.put(element);
                    Thread.sleep(timeout);
                }
            } catch (InterruptedException e) {
                log.error("Supplying interrupted : ", e);
                System.exit(-1);
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("Supplier object instance exception : ", e);
                System.exit(-1);
            }
        }
    }
}
