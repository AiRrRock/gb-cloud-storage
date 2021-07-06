package ru.aborichev.cloudstorage.core.messages.file;

import lombok.Getter;
import lombok.Setter;
import ru.aborichev.cloudstorage.core.session.UserSession;

@Getter
@Setter
public class FileDownloadMessage extends AbstractFileMessage {

    private String fileName;
    private String filePath;
    private byte[] data;

    public FileDownloadMessage(UserSession session) {
        super(session);
    }

    public FileDownloadMessage(UserSession session, String fileName, String filePath) {
        super(session);
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
