package model;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import view.FactoryForm;

public class Main {
    private static final String CONFIG_FILE_PATH = "src/config.properties";
    private static final Logger log = LogManager.getLogger(Main.class);

    public static void setLogging(boolean logging) {
        if (!logging) {
            Logger logger = LogManager.getRootLogger();
            Configurator.setLevel(logger.getName(), Level.OFF);
        }
    }

    public static void main(String[] args) {
        Configs configs = new Configs(CONFIG_FILE_PATH);
        setLogging(configs.getLogOn());
        Factory factory = new Factory(configs);
        new FactoryForm(factory).setVisible(true);
        log.info("set everything up, ready to start");
    }
}