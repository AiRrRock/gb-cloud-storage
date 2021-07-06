package ru.aborichev.cloudstorage.server.service.executors.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.aborichev.cloudstorage.core.messages.AbstractMessage;
import ru.aborichev.cloudstorage.core.messages.file.ChunkedFileUploadMessage;
import ru.aborichev.cloudstorage.core.model.Chunk;
import ru.aborichev.cloudstorage.core.model.ChunkedFile;
import ru.aborichev.cloudstorage.core.service.executors.CommandExecutor;
import ru.aborichev.cloudstorage.core.service.file.integrity.FileIntegrityCheckerService;
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
    private final Map<String, ChunkedFile> chunkedFiles;

    private static ChunkedFileUploadCommandExecutor instance;

    private ChunkedFileUploadCommandExecutor() {
        integrityCheckerService = Factory.getFileIntegrityChecker();
        chunkedFiles = new HashMap<>();
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
            try {
                if (infoNeeded) {
                    Path path = Paths.get(chunkedFileMessage.getServerFilePath());
                    if (!Files.isDirectory(path)) {
                        path = path.getParent();
                    }

                    path = Paths.get(path.toAbsolutePath().toString(), chunkedFileMessage.getFileName());
                    chunkedFileMessage.setServerFilePath(path.toAbsolutePath().toString());

                    Files.deleteIfExists(path);
                    Factory.getDirectoryService().deleteDirectory(Paths.get(path.getParent().toAbsolutePath().toString(), chunkedFileMessage.getOriginalName()));
                    Files.createFile(path);
                    return;
                }

                Chunk currentChunk = chunkedFileMessage.getChunk();
                if (integrityCheckerService.compareCheckSum(currentChunk.getData(), currentChunk.getMd5())) {
                    Path path = Paths.get(chunkedFileMessage.getServerFilePath());
                    chunkedFileMessage.setChunk(null);
                    byte[] currentData = new byte[currentChunk.getSize()];
                    System.arraycopy(currentChunk.getData(), 0, currentData, 0, currentChunk.getSize());
                    Files.write(path, currentData, StandardOpenOption.APPEND);
                    if (chunkedFileMessage.getNumberOfChunks() == chunkedFileMessage.getChunkNumber()) {
                        if (chunkedFileMessage.isCompressed()) {
                            Factory.getCompressionService().decompress(path, chunkedFileMessage.getOriginalName());
                            Files.deleteIfExists(path);
                        }
                    }

                    chunkedFileMessage.setChunkNumber(chunkedFileMessage.getChunkNumber() + 1);

                } else {
                    chunkedFileMessage.setChunk(null);
                }


            } catch (IOException e) {
                LOGGER.error(e);
            }


        }
    }

}
