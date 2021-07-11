package ru.aborichev.cloudstorage.core.messages.file;

import lombok.Getter;
import ru.aborichev.cloudstorage.core.session.UserSession;

@Getter
public class FileRemoveMessage extends AbstractFileMessage {
    private String filename;

    public FileRemoveMessage(final String filename, UserSession userSession) {
        super(userSession);
        this.filename = filename;
    }

}
