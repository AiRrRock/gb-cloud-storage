package ru.aborichev.cloudstorage.core.service.providers;

import ru.aborichev.cloudstorage.core.messages.AbstractMessage;

public interface CommandExecutorProviderService {
    void execute(AbstractMessage message);
}
