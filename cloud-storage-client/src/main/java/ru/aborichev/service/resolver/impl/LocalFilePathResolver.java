package ru.aborichev.service.resolver.impl;

import javafx.scene.control.TreeView;
import ru.aborichev.cloudstorage.core.model.FileNode;
import ru.aborichev.cloudstorage.core.service.environment.ConfigurationEnvironment;
import ru.aborichev.factory.Factory;
import ru.aborichev.service.resolver.PathResolver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalFilePathResolver implements PathResolver {
    private ConfigurationEnvironment environment;

    private static LocalFilePathResolver instance;

    private LocalFilePathResolver() {
        this.environment = Factory.getConfigurationEnvironment();
    }

    public static LocalFilePathResolver getInstance() {
        if (instance == null) {
            instance = new LocalFilePathResolver();
        }
        return instance;
    }


    @Override
    public Path resolvePathToSelectedFolder() {
        //TODO add exceptions handling and write to some default location.
        TreeView<FileNode> node = (TreeView<FileNode>) environment.getProperty("hostTreeView");
        Path path = Paths.get(node.getSelectionModel().getSelectedItem().getValue().getFilePath());
        Path filePath;
        if (Files.isDirectory(path)) {
            return Paths.get(node.getSelectionModel().getSelectedItem().getValue().getFilePath());
        } else {
            return Paths.get(path.getParent().toAbsolutePath().toString());
        }
    }

    @Override
    public Path resolvePathToDownloadFile(String fileName) {
        Path result = Paths.get(resolvePathToSelectedFolder().toAbsolutePath().toString(), fileName);
        return result;
    }
}
