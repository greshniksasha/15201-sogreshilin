package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Alexander on 07/06/2017.
 */
public class ClientConfigs {

    private static final String TYPE = "TYPE";
    private static final String SERVER_IP = "SERVER_IP";
    private static final String SERVER_PORT = "SERVER_PORT";
    private static final String LOG_ON = "LOG_ON";
    private String type;
    private String ip;
    private short port;
    private Boolean logOn;

    public static final Logger log = LogManager.getLogger(ClientConfigs.class);

    public ClientConfigs(String configFileName) {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(configFileName)) {
            properties.load(input);
            type = properties.getProperty(TYPE).toLowerCase();
            port = Short.parseShort(properties.getProperty(SERVER_PORT));
            ip = properties.getProperty(SERVER_IP);
            logOn = Boolean.parseBoolean(properties.getProperty(LOG_ON));
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

    public String getType() {
        return type;
    }

    public String getIp() {
        return ip;
    }

    public short getPort() {
        return port;
    }

    public Boolean getLogOn() {
        return logOn;
    }
}
