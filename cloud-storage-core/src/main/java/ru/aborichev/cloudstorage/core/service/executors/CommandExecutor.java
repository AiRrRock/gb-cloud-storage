package ru.aborichev.cloudstorage.core.service.executors;

import ru.aborichev.cloudstorage.core.messages.AbstractMessage;

public interface CommandExecutor {
    void execute(AbstractMessage message);
}
