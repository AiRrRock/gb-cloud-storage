package ru.aborichev.service.impl;

import ru.aborichev.cloudstorage.core.messages.AbstractMessage;
import ru.aborichev.cloudstorage.core.messages.file.ChunkedFileDownloadMessage;
import ru.aborichev.cloudstorage.core.messages.file.ChunkedFileUploadMessage;
import ru.aborichev.cloudstorage.core.messages.file.FileListMessage;
import ru.aborichev.cloudstorage.core.messages.user.AuthenticationUserMessage;
import ru.aborichev.cloudstorage.core.service.CommandExecutor;
import ru.aborichev.cloudstorage.core.service.CommandExecutorProviderService;

import java.util.HashMap;
import java.util.Map;

public class BasicCommandExecutorServiceProvider implements CommandExecutorProviderService {
    private static BasicCommandExecutorServiceProvider instance;
    private Map<Class<? extends AbstractMessage>, CommandExecutor> commands;

    private BasicCommandExecutorServiceProvider() {
        this.commands = new HashMap<>();
        commands.put(FileListMessage.class, FileListCommandExecutor.getInstance());
        commands.put(AuthenticationUserMessage.class, AuthenticationCommandExecutor.getInstance());
        commands.put(ChunkedFileDownloadMessage.class, ChunkedFileDownloadCommandExecutor.getInstance());
        commands.put(ChunkedFileUploadMessage.class, ChunkedFileUploadCommandExecutor.getInstance());
    }

    public static BasicCommandExecutorServiceProvider getInstance() {
        if (instance == null) {
            instance = new BasicCommandExecutorServiceProvider();
        }
        return instance;
    }

    public void execute(AbstractMessage message) {
        CommandExecutor executor = commands.get(message.getClass());
        if (executor != null) {
            executor.execute(message);
        }
    }
}
