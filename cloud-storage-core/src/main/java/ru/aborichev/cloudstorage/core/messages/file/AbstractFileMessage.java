package ru.aborichev.cloudstorage.core.messages.file;

import lombok.Getter;
import ru.aborichev.cloudstorage.core.messages.AbstractMessage;
import ru.aborichev.cloudstorage.core.session.UserSession;

import java.io.Serializable;

@Getter
public abstract class AbstractFileMessage  implements AbstractMessage, Serializable {
    private final UserSession session;

    public AbstractFileMessage(UserSession session) {
        this.session = session;
    }
}
