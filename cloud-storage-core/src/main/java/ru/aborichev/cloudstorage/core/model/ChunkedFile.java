package ru.aborichev.cloudstorage.core.model;

import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ChunkedFile {
    private int currentChunk;
    private int numberOfChunks;
    private String fileName;
    private String originalName;
    private String downloadID;
    private String remoteFilePath;
    private Path pathToResultingFile;
    private boolean completed;
    private boolean compressed;

    private Map<Integer, Chunk> chunks;

    public ChunkedFile(int numberOfChunks, String fileName) {
        this.chunks = new HashMap<>();
        this.currentChunk = 1;
        this.numberOfChunks = numberOfChunks;
        this.fileName = fileName;
        this.completed = false;
    }

    public synchronized void addChunk(int chunkNumber, Chunk newChunk) {
        chunks.put(chunkNumber, newChunk);
    }

    public synchronized Chunk getChunk() {
        Chunk chunk = chunks.get(currentChunk);
        chunks.remove(currentChunk);
        if (currentChunk == numberOfChunks) {
            completed = true;
        }
        return chunk;
    }

    public synchronized boolean isReadyToWriteNextChunk() {
        return chunks.containsKey(currentChunk);
    }

}
