package ru.aborichev.cloudstorage.server.service.executors.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.aborichev.cloudstorage.core.messages.AbstractMessage;
import ru.aborichev.cloudstorage.core.messages.file.ChunkedFileDownloadMessage;
import ru.aborichev.cloudstorage.core.messages.file.ChunkedFileUploadMessage;
import ru.aborichev.cloudstorage.core.model.Chunk;
import ru.aborichev.cloudstorage.core.service.environment.ConfigurationEnvironment;
import ru.aborichev.cloudstorage.core.service.executors.CommandExecutor;
import ru.aborichev.cloudstorage.core.service.file.chunks.FileChunkProviderService;
import ru.aborichev.cloudstorage.core.service.file.integrity.FileIntegrityCheckerService;
import ru.aborichev.cloudstorage.core.session.UserSession;
import ru.aborichev.cloudstorage.server.factory.Factory;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static ru.aborichev.cloudstorage.core.constants.MainConstants.MAX_CHUNK_SIZE;

public class ChunkedFileDownloadCommandExecutor implements CommandExecutor {
    private static final Logger LOGGER = LogManager.getLogger(ChunkedFileDownloadCommandExecutor.class);
    private final FileIntegrityCheckerService integrityCheckerService;
    private final FileChunkProviderService chunkProviderService;

    private static ChunkedFileDownloadCommandExecutor instance;

    private ChunkedFileDownloadCommandExecutor() {
        integrityCheckerService = Factory.getFileIntegrityChecker();
        chunkProviderService = Factory.getFileChunkProvider();
    }

    public static ChunkedFileDownloadCommandExecutor getInstance() {
        if (instance == null) {
            instance = new ChunkedFileDownloadCommandExecutor();
        }
        return instance;
    }

    @Override
    public void execute(AbstractMessage object) {
        if (object instanceof ChunkedFileDownloadMessage) {
            ChunkedFileDownloadMessage message = (ChunkedFileDownloadMessage) object;
            Path path = Paths.get(message.getRelativeFilePath());
            boolean provideInfo = false;

            if (message.getDownloadID() == null || message.getDownloadID().isEmpty()) {
                UUID uuid = UUID.randomUUID();
                message.setDownloadID(uuid.toString());
                provideInfo = true;
                boolean isCompressed = false;
                String originalName = path.toFile().getName();
                if (Files.isDirectory(path)) {
                    path = Factory.getCompressionService().compress(path);
                    isCompressed = true;
                }

                message.setCompressed(isCompressed);
                message.setFileName(path.toFile().getName());
                message.setRelativeFilePath(path.toAbsolutePath().toString());
                message.setOriginalName(originalName);

            }

            try {
                if (provideInfo) {
                    message.setNumberOfChunks(chunkProviderService.calculateChunkAmount(path, MAX_CHUNK_SIZE));
                }
                LOGGER.info(String.format("Preparing to send chunk N%d for file [%s]", message.getChunkNumber(), message.getRelativeFilePath()));
                Chunk currentChunk = chunkProviderService.getChunk(path, message.getChunkNumber(), MAX_CHUNK_SIZE);
                currentChunk.setChunkNumber(message.getChunkNumber());
                currentChunk.setMd5(integrityCheckerService.calculateCheckSum(currentChunk.getData()));
                message.setCurrentChunk(currentChunk);
                LOGGER.info("Sending chunk № " + message.getChunkNumber());

            } catch (IOException e) {
                LOGGER.error(e);
            }
        }
    }
}
