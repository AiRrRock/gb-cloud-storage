package ru.aborichev.cloudstorage.core.service.file.chunks.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.aborichev.cloudstorage.core.model.Chunk;
import ru.aborichev.cloudstorage.core.service.file.chunks.FileChunkProviderService;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

public class BasicFileChunkProviderService implements FileChunkProviderService {
    private static final Logger LOGGER = LogManager.getLogger(BasicFileChunkProviderService.class);
    private static BasicFileChunkProviderService instance;

    private BasicFileChunkProviderService() {

    }

    public static BasicFileChunkProviderService getInstance() {
        if (instance == null) {
            instance = new BasicFileChunkProviderService();
        }
        return instance;
    }

    @Override
    public Chunk getChunk(Path filePath, int chunkNumber, int chunkSize) throws IOException {
        if (Files.exists(filePath)) {
            try (RandomAccessFile file = new RandomAccessFile(filePath.toFile(), "r")) {
                byte[] bytes = new byte[chunkSize];
                file.seek((chunkNumber - 1) * chunkSize);
                int currentChunkSize = file.read(bytes, 0, chunkSize);
                Chunk currentChunk = new Chunk();
                currentChunk.setSize(currentChunkSize);
                currentChunk.setData(bytes);
                LOGGER.info("Got chunk");
                return currentChunk;
            }
        } else {
            throw new IOException("File doesn't exist");
        }
    }

    @Override
    public int calculateChunkAmount(Path filePath, int chunkSize) throws IOException {
        if (Files.exists(filePath)) {
            try (RandomAccessFile file = new RandomAccessFile(filePath.toFile(), "r")) {
                int numberOfChunks = (int) file.length() / chunkSize + (file.length() % chunkSize > 0 ? 1 : 0);
                return numberOfChunks;
            }
        } else {
            throw new IOException("File doesn't exist");
        }
    }
}
