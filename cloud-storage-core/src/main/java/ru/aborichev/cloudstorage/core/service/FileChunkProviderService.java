package ru.aborichev.cloudstorage.core.service;

import ru.aborichev.cloudstorage.core.model.Chunk;

import java.io.IOException;
import java.nio.file.Path;

public interface FileChunkProviderService {
    Chunk getChunk(Path filePath, int chunkNumber, int chunkSize) throws IOException;
    int calculateChunkAmount(Path filePath, int chunkSize) throws IOException;
}
