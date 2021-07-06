package ru.aborichev.service.executors.impl;

import javafx.scene.control.TreeView;
import ru.aborichev.cloudstorage.core.messages.AbstractMessage;
import ru.aborichev.cloudstorage.core.messages.file.FileDownloadMessage;
import ru.aborichev.cloudstorage.core.model.FileNode;
import ru.aborichev.cloudstorage.core.service.environment.ConfigurationEnvironment;
import ru.aborichev.cloudstorage.core.service.executors.CommandExecutor;
import ru.aborichev.factory.Factory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDownloadCommandExecutor implements CommandExecutor {

    private ConfigurationEnvironment environment;

    private static FileDownloadCommandExecutor instance;

    private FileDownloadCommandExecutor() {
        this.environment = Factory.getConfigurationEnvironment();
    }

    public static FileDownloadCommandExecutor getInstance() {
        if (instance == null) {
            instance = new FileDownloadCommandExecutor();
        }
        return instance;
    }

    @Override
    public void execute(AbstractMessage object) {
        FileDownloadMessage file = null;
        if (object instanceof FileDownloadMessage) {
            file = (FileDownloadMessage) object;
            String name = file.getFileName();
            byte[] data = file.getData();

            TreeView<FileNode> node = (TreeView<FileNode>) environment.getProperty("hostTreeView");
            Path path = Paths.get(node.getSelectionModel().getSelectedItem().getValue().getFilePath());
            Path filePath;
            if (Files.isDirectory(path)) {
                filePath = Paths.get(node.getSelectionModel().getSelectedItem().getValue().getFilePath(), name);
            } else {
                filePath = Paths.get(path.getParent().toAbsolutePath().toString(), name);
            }

            try {
                Files.write(filePath, data);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
