package ru.aborichev.cloudstorage.server;

import org.apache.logging.log4j.LogManager;
import ru.aborichev.cloudstorage.server.factory.Factory;
import org.apache.logging.log4j.Logger;


public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static final String BASE_DIR = "./server_storage/";

    public static void main(String[] args) {
        LOGGER.info("Starting server app");
        Factory.getConfigurationEnvironment().setProperty("baseDir", BASE_DIR);
        Factory.getServerService().startServer();
    }
}
