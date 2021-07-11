package ru.aborichev.cloudstorage.server.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.aborichev.cloudstorage.core.messages.AbstractMessage;
import ru.aborichev.cloudstorage.core.messages.file.ChunkedFileUploadMessage;
import ru.aborichev.cloudstorage.core.model.Chunk;
import ru.aborichev.cloudstorage.core.model.ChunkedFile;
import ru.aborichev.cloudstorage.core.service.CommandExecutor;
import ru.aborichev.cloudstorage.core.service.FileIntegrityCheckerService;
import ru.aborichev.cloudstorage.server.factory.Factory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class ChunkedFileUploadCommandExecutor implements CommandExecutor {
    private static final Logger LOGGER = LogManager.getLogger(ChunkedFileUploadCommandExecutor.class);
    private final FileIntegrityCheckerService integrityCheckerService;

    private static ChunkedFileUploadCommandExecutor instance;

    private ChunkedFileUploadCommandExecutor() {
        integrityCheckerService = Factory.getFileIntegrityChecker();
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
            } else {
                processChunk(chunkedFileMessage);
            }

        }
    }

    void prepareForUpload(ChunkedFileUploadMessage chunkedFileMessage) {
        Path path = Paths.get(chunkedFileMessage.getServerFilePath());

        if (!Files.isDirectory(path)) {
            path = path.getParent();
        }

        path = Paths.get(path.toAbsolutePath().toString(), chunkedFileMessage.getFileName());
        chunkedFileMessage.setServerFilePath(path.toAbsolutePath().toString());

        try {
            Files.deleteIfExists(path);
            Factory.getDirectoryService().deleteDirectory(Paths.get(path.getParent().toAbsolutePath().toString(), chunkedFileMessage.getOriginalName()));
            Files.createFile(path);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    void processChunk(ChunkedFileUploadMessage chunkedFileMessage) {
        Chunk currentChunk = chunkedFileMessage.getChunk();

        if (integrityCheckerService.compareCheckSum(currentChunk.getData(), currentChunk.getMd5())) {
            Path path = Paths.get(chunkedFileMessage.getServerFilePath());
            byte[] currentData = new byte[currentChunk.getSize()];
            System.arraycopy(currentChunk.getData(), 0, currentData, 0, currentChunk.getSize());

            try {
                Files.write(path, currentData, StandardOpenOption.APPEND);
                if (chunkedFileMessage.getNumberOfChunks() == chunkedFileMessage.getChunkNumber()) {
                    if (chunkedFileMessage.isCompressed()) {
                        Factory.getCompressionService().decompress(path, chunkedFileMessage.getOriginalName());
                        Files.deleteIfExists(path);
                    }
                }
            } catch (IOException e) {
                LOGGER.error(e);
            }
            chunkedFileMessage.setChunkNumber(chunkedFileMessage.getChunkNumber() + 1);
        }
        chunkedFileMessage.setChunk(null);

    }


}
