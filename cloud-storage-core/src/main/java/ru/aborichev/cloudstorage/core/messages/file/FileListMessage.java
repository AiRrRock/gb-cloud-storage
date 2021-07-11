package ru.aborichev.cloudstorage.core.messages.file;

import lombok.Getter;
import lombok.Setter;
import ru.aborichev.cloudstorage.core.model.FileNode;
import ru.aborichev.cloudstorage.core.session.UserSession;

@Getter
@Setter
public class FileListMessage extends AbstractFileMessage {
    private FileNode fileNode;

    public FileListMessage(UserSession session) {
        super(session);
    }

}
