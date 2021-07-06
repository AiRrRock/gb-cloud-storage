package ru.aborichev.cloudstorage.server.service.executors.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.aborichev.cloudstorage.core.messages.AbstractMessage;
import ru.aborichev.cloudstorage.core.messages.file.FileListMessage;
import ru.aborichev.cloudstorage.core.model.FileNode;
import ru.aborichev.cloudstorage.core.service.environment.ConfigurationEnvironment;
import ru.aborichev.cloudstorage.core.service.executors.CommandExecutor;
import ru.aborichev.cloudstorage.server.factory.Factory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileListCommandExecutor implements CommandExecutor {
    private static final Logger LOGGER = LogManager.getLogger(FileListCommandExecutor.class);
    private static final ConfigurationEnvironment configurations = Factory.getConfigurationEnvironment();

    private static FileListCommandExecutor instance;

    private FileListCommandExecutor() {

    }

    public static FileListCommandExecutor getInstance() {
        if (instance == null) {
            instance = new FileListCommandExecutor();
        }
        return instance;
    }

    @Override
    public void execute(AbstractMessage object) {
        FileListMessage message = (FileListMessage) object;
        String baseUserDir = Factory.getUserDirectoryResolver().resolveByName(message.getSession().getUserName());
        LOGGER.info("Preparing to send file structure ");
        Path baseDirPath = Paths.get(baseUserDir);
        if(!Files.exists(baseDirPath)){
            try {
                Files.createDirectories(baseDirPath);
            } catch (IOException e) {
                LOGGER.error(e);
                return;
            }
        }
        List<Path> files = new ArrayList<>();
        try {
            Files.newDirectoryStream(Paths.get(baseUserDir))
                    .forEach(path -> files.add(path));

            FileNode root = new FileNode("root", baseDirPath.toString());
            root.setChildren(new ArrayList<>());
            for (Path path : files) {
                root.getChildren().add(populateFileNode(path));
            }
            message.setFileNode(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private FileNode populateFileNode(Path basePath) throws IOException {
        FileNode node = new FileNode(basePath.getFileName().toString(), basePath.toAbsolutePath().toString());
        if (basePath.toFile().isDirectory()) {
            List<FileNode> children = new ArrayList<>();
            Files.newDirectoryStream(basePath).forEach(path -> {
                try {
                    children.add(populateFileNode(path));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            node.setChildren(children);
        } else {
            node.setLeaf(true);
        }
        return node;
    }
}
