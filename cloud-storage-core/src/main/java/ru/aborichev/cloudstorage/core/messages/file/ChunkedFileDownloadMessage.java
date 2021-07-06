package ru.aborichev.cloudstorage.core.messages.file;

import lombok.Getter;
import lombok.Setter;
import ru.aborichev.cloudstorage.core.session.UserSession;
import ru.aborichev.cloudstorage.core.model.Chunk;

@Getter
@Setter
public class ChunkedFileDownloadMessage extends AbstractFileMessage {
    private String fileName;
    private String originalName;
    private String relativeFilePath;
    private int numberOfChunks;
    private int chunkNumber;
    private boolean compressed;
    private String downloadID;
    private Chunk currentChunk;

    public ChunkedFileDownloadMessage(UserSession session, String fileName, String relativeFilePath, int chunkNumber) {
        super(session);
        this.fileName = fileName;
        this.relativeFilePath = relativeFilePath;
        this.chunkNumber = chunkNumber;
    }

    public ChunkedFileDownloadMessage(UserSession session, String fileName, String relativeFilePath, String downloadID, int chunkNumber) {
        super(session);
        this.fileName = fileName;
        this.relativeFilePath = relativeFilePath;
        this.downloadID = downloadID;
        this.chunkNumber = chunkNumber;
    }
}
