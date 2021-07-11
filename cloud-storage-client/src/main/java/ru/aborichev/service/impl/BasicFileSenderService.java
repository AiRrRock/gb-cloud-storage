package ru.aborichev.service.impl;

import ru.aborichev.cloudstorage.core.messages.file.ChunkedFileUploadMessage;
import ru.aborichev.cloudstorage.core.session.UserSession;
import ru.aborichev.factory.Factory;
import ru.aborichev.service.FileSenderService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BasicFileSenderService implements FileSenderService {
    private static BasicFileSenderService instance;

    private BasicFileSenderService() {
    }

    public static BasicFileSenderService getInstance() {
        if (instance == null) {
            instance = new BasicFileSenderService();
        }
        return instance;
    }

    @Override
    public void sendFile(String localFilePath, String remoteFilePath) {
        Path path = Paths.get(localFilePath);
        if (Files.exists(path)) {
            boolean isCompressed = false;
            String originalName = path.toFile().getName();
            UserSession session = (UserSession) Factory.getConfigurationEnvironment().getProperty("session");
            if (Files.isDirectory(path)) {
                path = Factory.getCompressionService().compress(path);
                isCompressed = true;
            }
            ChunkedFileUploadMessage fileUploadMessage = new ChunkedFileUploadMessage(session, path.toAbsolutePath().toString(), remoteFilePath, isCompressed, 0);
            fileUploadMessage.setFileName(path.toFile().getName());
            fileUploadMessage.setOriginalName(originalName);
            Factory.getNetworkService().sendCommand(fileUploadMessage);
        }
    }
}
