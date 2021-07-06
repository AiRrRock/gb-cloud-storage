package ru.aborichev.cloudstorage.server.service.providers;

import ru.aborichev.cloudstorage.core.messages.AbstractMessage;
import ru.aborichev.cloudstorage.core.messages.file.ChunkedFileDownloadMessage;
import ru.aborichev.cloudstorage.core.messages.file.ChunkedFileUploadMessage;
import ru.aborichev.cloudstorage.core.messages.file.FileDownloadMessage;
import ru.aborichev.cloudstorage.core.messages.file.FileListMessage;
import ru.aborichev.cloudstorage.core.service.executors.CommandExecutor;
import ru.aborichev.cloudstorage.core.service.providers.CommandExecutorProviderService;
import ru.aborichev.cloudstorage.server.service.executors.impl.ChunkedFileDownloadCommandExecutor;
import ru.aborichev.cloudstorage.server.service.executors.impl.ChunkedFileUploadCommandExecutor;
import ru.aborichev.cloudstorage.server.service.executors.impl.FileDownloadCommandExecutor;
import ru.aborichev.cloudstorage.server.service.executors.impl.FileListCommandExecutor;

import java.util.HashMap;
import java.util.Map;

public class BasicCommandExecutorProviderService implements CommandExecutorProviderService {
    private static BasicCommandExecutorProviderService instance;
    private Map<Class<? extends AbstractMessage>, CommandExecutor> commands;

    private BasicCommandExecutorProviderService() {
        this.commands = new HashMap<>();
        commands.put(FileListMessage.class, FileListCommandExecutor.getInstance());
        commands.put(FileDownloadMessage.class, FileDownloadCommandExecutor.getInstance());
        commands.put(ChunkedFileDownloadMessage.class, ChunkedFileDownloadCommandExecutor.getInstance());
        commands.put(ChunkedFileUploadMessage.class, ChunkedFileUploadCommandExecutor.getInstance());
    }

    public static BasicCommandExecutorProviderService getInstance() {
        if (instance == null) {
            instance = new BasicCommandExecutorProviderService();
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