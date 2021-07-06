package ru.aborichev.cloudstorage.server.service.executors.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.aborichev.cloudstorage.core.messages.AbstractMessage;
import ru.aborichev.cloudstorage.core.messages.file.FileDownloadMessage;
import ru.aborichev.cloudstorage.core.service.environment.ConfigurationEnvironment;
import ru.aborichev.cloudstorage.core.service.executors.CommandExecutor;
import ru.aborichev.cloudstorage.server.factory.Factory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FileDownloadCommandExecutor implements CommandExecutor {
    private static final Logger LOGGER = LogManager.getLogger(FileListCommandExecutor.class);
    private static final ConfigurationEnvironment configurations = Factory.getConfigurationEnvironment();

    private static FileDownloadCommandExecutor instance;

    private FileDownloadCommandExecutor() {

    }

    public static FileDownloadCommandExecutor getInstance() {
        if (instance == null) {
            instance = new FileDownloadCommandExecutor();
        }
        return instance;
    }

    @Override
    public void execute(AbstractMessage object) {
        FileDownloadMessage message = (FileDownloadMessage) object;
        Path path = Paths.get(message.getFilePath());
        LOGGER.info("Preparing to send file");
        if(Files.exists(path)){
            try {
                message.setData(Files.readAllBytes(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
