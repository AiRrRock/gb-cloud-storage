package ru.aborichev.cloudstorage.core.service;

import ru.aborichev.cloudstorage.core.messages.AbstractMessage;

public interface CommandExecutorProviderService {
    void execute(AbstractMessage message);
}
