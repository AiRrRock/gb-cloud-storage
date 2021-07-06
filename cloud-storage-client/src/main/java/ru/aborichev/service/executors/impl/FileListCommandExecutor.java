package ru.aborichev.service.executors.impl;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import ru.aborichev.cloudstorage.core.messages.AbstractMessage;
import ru.aborichev.cloudstorage.core.messages.file.FileListMessage;
import ru.aborichev.cloudstorage.core.model.FileNode;
import ru.aborichev.factory.Factory;
import ru.aborichev.cloudstorage.core.service.executors.CommandExecutor;
import ru.aborichev.view.treeitems.RemoteFileNodeTreeItem;

public class FileListCommandExecutor implements CommandExecutor {
    private final TreeView<FileNode> serverTreeView;
    private final HBox loginHBox;
    private final Button logout;

    private static FileListCommandExecutor instance;

    private FileListCommandExecutor(TreeView<FileNode> serverTreeView, HBox loginBox, Button logout) {
        this.serverTreeView = serverTreeView;
        this.loginHBox = loginBox;
        this.logout = logout;
    }

    public static FileListCommandExecutor getInstance() {
        if (instance == null) {
            instance = new FileListCommandExecutor((TreeView<FileNode>) Factory.getConfigurationEnvironment().getProperty("serverTreeView"),
                    (HBox) Factory.getConfigurationEnvironment().getProperty("loginHBox"),
                    (Button) Factory.getConfigurationEnvironment().getProperty("logoutButton"));
        }
        return instance;
    }

    @Override
    public void execute(AbstractMessage message) {
        FileNode node = ((FileListMessage) message).getFileNode();
        if (node != null) {
            RemoteFileNodeTreeItem item = new RemoteFileNodeTreeItem(node);
            Platform.runLater((new Runnable() {
                @Override
                public void run() {
                    serverTreeView.setRoot(item);
                    loginHBox.setVisible(false);
                    loginHBox.setManaged(false);
                    logout.setManaged(true);
                    logout.setVisible(true);
                }
            }));
        }
    }
}