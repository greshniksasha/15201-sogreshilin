package model;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Alexander on 13/04/2017.
 */
public class Parser {

    public int engineWarehouseSize;
    public int bodyWarehouseSize;
    public int accessoryWarehouseSize;
    public int carWarehouseSize;

    public int taskQueueSize;
    public int threadPoolSize;

    public int accessorySupplierCount;
    public int dealerCount;

    public int engineSupplierTimeout;
    public int bodySupplierTimeout;
    public int accessorySupplierTimeout;
    public int dealerTimeout;

    public Boolean logSales;

    public static final Logger log = Logger.getLogger(Assembly.class);

    public Parser(String configFileName) {
        try (FileInputStream input = new FileInputStream(configFileName)) {
            Properties properties = new Properties();
            properties.load(input);
            engineWarehouseSize = Integer.parseInt(properties.getProperty("ENGINE_WAREHOUSE_SIZE"));
            bodyWarehouseSize = Integer.parseInt(properties.getProperty("BODY_WAREHOUSE_SIZE"));
            accessoryWarehouseSize = Integer.parseInt(properties.getProperty("ACCESSORY_WAREHOUSE_SIZE"));
            carWarehouseSize = Integer.parseInt(properties.getProperty("CAR_WAREHOUSE_SIZE"));
            taskQueueSize = Integer.parseInt(properties.getProperty("TASK_QUEUE_SIZE"));
            accessorySupplierCount = Integer.parseInt(properties.getProperty("ACCESSORY_SUPPLIER_COUNT"));
            dealerCount = Integer.parseInt(properties.getProperty("DEALER_COUNT"));
            threadPoolSize = Integer.parseInt(properties.getProperty("WORKER_COUNT"));
            engineSupplierTimeout = Integer.parseInt(properties.getProperty("ENGINE_SUPPLIER_TIMEOUT"));
            bodySupplierTimeout = Integer.parseInt(properties.getProperty("BODY_SUPPLIER_TIMEOUT"));
            accessorySupplierTimeout = Integer.parseInt(properties.getProperty("ACCESSORY_SUPPLIER_TIMEOUT"));
            dealerTimeout = Integer.parseInt(properties.getProperty("DEALER_TIMEOUT"));
            logSales = Boolean.parseBoolean(properties.getProperty("LOG_SALES"));
        } catch (FileNotFoundException e) {
            log.error("Config file not found : ", e);
            System.exit(-1);
        } catch (IOException e) {
            log.error("I/O Exception : ", e);
            System.exit(-1);
        } catch (NumberFormatException e) {
            log.error("Could not parse config file : ", e);
            System.exit(-1);
        }
    }

}
