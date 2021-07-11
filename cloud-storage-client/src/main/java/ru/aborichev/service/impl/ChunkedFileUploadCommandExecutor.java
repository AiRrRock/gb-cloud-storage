package ru.aborichev.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.aborichev.cloudstorage.core.messages.AbstractMessage;
import ru.aborichev.cloudstorage.core.messages.file.ChunkedFileUploadMessage;
import ru.aborichev.cloudstorage.core.messages.file.FileListMessage;
import ru.aborichev.cloudstorage.core.model.Chunk;
import ru.aborichev.cloudstorage.core.service.CommandExecutor;
import ru.aborichev.cloudstorage.core.service.FileChunkProviderService;
import ru.aborichev.cloudstorage.core.service.FileIntegrityCheckerService;
import ru.aborichev.cloudstorage.core.session.UserSession;
import ru.aborichev.factory.Factory;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static ru.aborichev.cloudstorage.core.constants.MainConstants.MAX_CHUNK_SIZE;

public class ChunkedFileUploadCommandExecutor implements CommandExecutor {
    private static ChunkedFileUploadCommandExecutor instance;
    private static final Logger LOGGER = LogManager.getLogger(ChunkedFileUploadCommandExecutor.class);
    private final Map<String, String> chunkedFiles;
    private final FileIntegrityCheckerService integrityCheckerService;
    private final FileChunkProviderService fileChunkProviderService;


    private ChunkedFileUploadCommandExecutor() {
        this.chunkedFiles = new HashMap<>();
        this.integrityCheckerService = Factory.getFileIntegrityChecker();
        this.fileChunkProviderService = Factory.getFileChunkProvider();
    }

    public static ChunkedFileUploadCommandExecutor getInstance() {
        if (instance == null) {
            instance = new ChunkedFileUploadCommandExecutor();
        }
        return instance;
    }

    @Override
    public void execute(AbstractMessage message) {
        ChunkedFileUploadMessage chunkedFileMessage = null;
        LOGGER.debug("Got message of type " + message.getClass());
        if (message instanceof ChunkedFileUploadMessage) {
            chunkedFileMessage = (ChunkedFileUploadMessage) message;
            boolean infoNeeded = chunkedFileMessage.getChunkNumber() == 0;
            if (infoNeeded) {
                prepareForUpload(chunkedFileMessage);
            }
            processChunk(chunkedFileMessage);
        }
    }

    void prepareForUpload(ChunkedFileUploadMessage chunkedFileMessage) {
        try {
            chunkedFileMessage.setNumberOfChunks(fileChunkProviderService.calculateChunkAmount(Paths.get(chunkedFileMessage.getAbsoluteLocalFilePath()), MAX_CHUNK_SIZE));
            chunkedFileMessage.setChunkNumber(1);
            chunkedFiles.put(chunkedFileMessage.getServerFilePath(), chunkedFileMessage.getAbsoluteLocalFilePath());
        } catch (IOException e) {
            LOGGER.error(e);
        }

    }

    void processChunk(ChunkedFileUploadMessage chunkedFileMessage) {
        if (chunkedFileMessage.getChunkNumber() > chunkedFileMessage.getNumberOfChunks()) {
            cleanup(chunkedFileMessage);
        }

        Chunk currentChunk = null;
        try {
            currentChunk = fileChunkProviderService.getChunk(Paths.get(chunkedFileMessage.getAbsoluteLocalFilePath()), chunkedFileMessage.getChunkNumber(), MAX_CHUNK_SIZE);
            currentChunk.setChunkNumber(chunkedFileMessage.getChunkNumber());
            currentChunk.setMd5(integrityCheckerService.calculateCheckSum(currentChunk.getData()));
            chunkedFileMessage.setChunk(currentChunk);
        } catch (IOException e) {
            LOGGER.error(e);
        }

        Factory.getNetworkService().sendCommand(chunkedFileMessage);
    }

    void cleanup(ChunkedFileUploadMessage chunkedFileMessage) {
        if (chunkedFileMessage.isCompressed()) {
            try {
                String localFile = chunkedFiles.get(chunkedFileMessage.getServerFilePath());
                Files.deleteIfExists(Paths.get(localFile));
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }

        chunkedFiles.remove(chunkedFileMessage.getServerFilePath());
        FileListMessage fileListMessage = new FileListMessage((UserSession) Factory.getConfigurationEnvironment().getProperty("session"));
        Factory.getNetworkService().sendCommand(fileListMessage);
    }

}

