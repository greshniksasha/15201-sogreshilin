package model;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class Configs {
    private static final String ENGINE_WAREHOUSE_SIZE = "ENGINE_WAREHOUSE_SIZE";
    private static final String BODY_WAREHOUSE_SIZE = "BODY_WAREHOUSE_SIZE";
    private static final String ACCESSORY_WAREHOUSE_SIZE = "ACCESSORY_WAREHOUSE_SIZE";
    private static final String CAR_WAREHOUSE_SIZE = "CAR_WAREHOUSE_SIZE";
    private static final String TASK_QUEUE_SIZE = "TASK_QUEUE_SIZE";
    private static final String ACCESSORY_SUPPLIER_COUNT = "ACCESSORY_SUPPLIER_COUNT";
    private static final String DEALER_COUNT = "DEALER_COUNT";
    private static final String WORKER_COUNT = "WORKER_COUNT";
    private static final String ENGINE_SUPPLIER_TIMEOUT = "ENGINE_SUPPLIER_TIMEOUT";
    private static final String BODY_SUPPLIER_TIMEOUT = "BODY_SUPPLIER_TIMEOUT";
    private static final String ACCESSORY_SUPPLIER_TIMEOUT = "ACCESSORY_SUPPLIER_TIMEOUT";
    private static final String DEALER_TIMEOUT = "DEALER_TIMEOUT";
    private static final String LOG_ON = "LOG_ON";

    private int engineWarehouseSize;
    private int bodyWarehouseSize;
    private int accessoryWarehouseSize;
    private int carWarehouseSize;

    private int taskQueueSize;
    private int threadPoolSize;

    private int accessorySupplierCount;
    private int dealerCount;

    private int engineSupplierTimeout;
    private int bodySupplierTimeout;
    private int accessorySupplierTimeout;
    private int dealerTimeout;

    private Boolean logOn;

    public static final Logger log = LogManager.getLogger(Assembly.class);

    public Configs(String configFileName) {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(configFileName)) {
            properties.load(input);
        } catch (FileNotFoundException e) {
            log.error("Config file not found");
            System.exit(-1);
        } catch (IOException e) {
            log.error("I/O Exception : ", e);
            System.exit(-1);
        } catch (NumberFormatException e) {
            log.error("Could not parse config file : ", e);
            System.exit(-1);
        }
        engineWarehouseSize =       Integer.parseInt(properties.getProperty(ENGINE_WAREHOUSE_SIZE));
        bodyWarehouseSize =         Integer.parseInt(properties.getProperty(BODY_WAREHOUSE_SIZE));
        accessoryWarehouseSize =    Integer.parseInt(properties.getProperty(ACCESSORY_WAREHOUSE_SIZE));
        carWarehouseSize =          Integer.parseInt(properties.getProperty(CAR_WAREHOUSE_SIZE));
        taskQueueSize =             Integer.parseInt(properties.getProperty(TASK_QUEUE_SIZE));
        accessorySupplierCount =    Integer.parseInt(properties.getProperty(ACCESSORY_SUPPLIER_COUNT));
        dealerCount =               Integer.parseInt(properties.getProperty(DEALER_COUNT));
        threadPoolSize =            Integer.parseInt(properties.getProperty(WORKER_COUNT));
        engineSupplierTimeout =     Integer.parseInt(properties.getProperty(ENGINE_SUPPLIER_TIMEOUT));
        bodySupplierTimeout =       Integer.parseInt(properties.getProperty(BODY_SUPPLIER_TIMEOUT));
        accessorySupplierTimeout =  Integer.parseInt(properties.getProperty(ACCESSORY_SUPPLIER_TIMEOUT));
        dealerTimeout =             Integer.parseInt(properties.getProperty(DEALER_TIMEOUT));
        logOn =                     Boolean.parseBoolean(properties.getProperty(LOG_ON));
    }

    public int getEngineWarehouseSize() {
        return engineWarehouseSize;
    }

    public int getBodyWarehouseSize() {
        return bodyWarehouseSize;
    }

    public int getAccessoryWarehouseSize() {
        return accessoryWarehouseSize;
    }

    public int getCarWarehouseSize() {
        return carWarehouseSize;
    }

    public int getTaskQueueSize() {
        return taskQueueSize;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public int getAccessorySupplierCount() {
        return accessorySupplierCount;
    }

    public int getDealerCount() {
        return dealerCount;
    }

    public int getEngineSupplierTimeout() {
        return engineSupplierTimeout;
    }

    public int getBodySupplierTimeout() {
        return bodySupplierTimeout;
    }

    public int getAccessorySupplierTimeout() {
        return accessorySupplierTimeout;
    }

    public int getDealerTimeout() {
        return dealerTimeout;
    }

    public Boolean getLogOn() {
        return logOn;
    }
}
