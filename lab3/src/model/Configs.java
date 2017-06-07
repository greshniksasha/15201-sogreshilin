package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

/**
 * Created by Alexander on 07/06/2017.
 */
public class Configs {
    enum SerializationType { STANDARD, XML };

    private static final String SERIALIZATION = "SERIALIZATION";
    private SerializationType serializationType;

    public static final Logger log = LogManager.getLogger(Configs.class);

    public Configs(String configFileName) {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(configFileName)) {
            properties.load(input);
            serializationType = SerializationType.valueOf(properties.getProperty(SERIALIZATION));
        } catch (FileNotFoundException e) {
            log.error("Config file not found");
            System.exit(-1);
        } catch (IOException e) {
            log.error("I/O Exception : ", e);
            System.exit(-1);
        } catch (NumberFormatException e) {
            log.error("Could not parse config file : ", e);
            System.exit(-1);
        } catch (IllegalArgumentException e) {
            log.error("Config os incorrect");
            System.exit(-1);
        }
    }

    public SerializationType getSerializationType() {
        return serializationType;
    }
}
