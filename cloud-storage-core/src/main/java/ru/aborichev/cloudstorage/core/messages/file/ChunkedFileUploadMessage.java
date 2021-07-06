package ru.aborichev.cloudstorage.core.messages.file;

import lombok.Getter;
import lombok.Setter;
import ru.aborichev.cloudstorage.core.model.Chunk;
import ru.aborichev.cloudstorage.core.session.UserSession;


@Getter
@Setter
public class ChunkedFileUploadMessage extends AbstractFileMessage {
    private String fileName;
    private String originalName;
    private String absoluteLocalFilePath;
    private String serverFilePath;
    private int numberOfChunks;
    private int chunkNumber;
    private boolean compressed;
    private Chunk chunk;

    public ChunkedFileUploadMessage(UserSession session, String absoluteLocalFilePath, String serverFilePath, boolean compressed, int chunkNumber) {
        super(session);
        this.absoluteLocalFilePath = absoluteLocalFilePath;
        this.serverFilePath = serverFilePath;
        this.chunkNumber = chunkNumber;
        this.compressed = compressed;

    }
}
