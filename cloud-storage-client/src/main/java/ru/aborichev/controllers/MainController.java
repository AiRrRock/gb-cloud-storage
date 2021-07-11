package ru.aborichev.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import ru.aborichev.cloudstorage.core.messages.AbstractMessage;
import ru.aborichev.cloudstorage.core.messages.file.ChunkedFileDownloadMessage;
import ru.aborichev.cloudstorage.core.messages.file.FileListMessage;
import ru.aborichev.cloudstorage.core.messages.user.AuthenticationUserMessage;
import ru.aborichev.cloudstorage.core.model.FileNode;
import ru.aborichev.cloudstorage.core.service.CommandExecutorProviderService;
import ru.aborichev.cloudstorage.core.service.ConfigurationEnvironment;
import ru.aborichev.cloudstorage.core.session.UserSession;
import ru.aborichev.view.treeitems.LocalFileNodeTreeItem;
import ru.aborichev.factory.Factory;
import ru.aborichev.service.NetworkService;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainController implements Initializable {

    private AtomicBoolean canProceed = new AtomicBoolean(false);
    @FXML
    TreeView<FileNode> hostTreeView;
    @FXML
    TreeView<FileNode> serverTreeView;
    @FXML
    PasswordField password;
    @FXML
    TextField userName;
    @FXML
    Button login;
    @FXML
    Button logout;
    @FXML
    HBox topBox;

    private NetworkService networkService;
    private CommandExecutorProviderService executor;
    private ConfigurationEnvironment configurations;

    public MainController() {
    }

    @FXML
    public void upload() {
        //TODO add implementation for the upload method
        String localFilePath  = hostTreeView.getSelectionModel().getSelectedItem().getValue().getFilePath() ;
        String remoteFilePath = serverTreeView.getSelectionModel().getSelectedItem().getValue().getFilePath();
        Factory.getFileSenderService().sendFile(localFilePath, remoteFilePath);
    }

    @FXML
    public void download() {
        FileNode toDownload = serverTreeView.getSelectionModel().getSelectedItem().getValue();
        networkService.sendCommand(new ChunkedFileDownloadMessage((UserSession) configurations.getProperty("session"), toDownload.getName(), toDownload.getFilePath(), 1));
    }


    @FXML
    public void login() throws InterruptedException {
        networkService.sendCommand(new AuthenticationUserMessage(userName.getText(), password.getText()));
        int iterationsToWait = 5;
        int i = 0;
        while (configurations.getProperty("session") == null && i < iterationsToWait) {
            i++;
            Thread.sleep(100);
        }
        populateServerFileSystem();

    }

    public void logout() {
        serverTreeView.setRoot(null);
        configurations.setProperty("session", null);
        topBox.setManaged(true);
        topBox.setVisible(true);
        logout.setVisible(false);
        logout.setManaged(false);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        networkService = Factory.getNetworkService();
        configurations = Factory.getConfigurationEnvironment();
        createCommandResultHandler();
        configurations.setProperty("hostTreeView", hostTreeView);
        configurations.setProperty("serverTreeView", serverTreeView);
        configurations.setProperty("loginHBox", topBox);
        configurations.setProperty("logoutButton", logout);
        executor = Factory.getCommandExecutorProvider();
        populateLocalFileSystem();

    }

    private void populateLocalFileSystem() {
        File[] roots = File.listRoots();
        TreeItem<FileNode> root = new TreeItem(new File("This PC"));
        for (File file : roots) {
            root.getChildren().add(new LocalFileNodeTreeItem(file.getName(), file.getAbsolutePath()));
        }
        hostTreeView.setRoot(root);
    }

    private void populateServerFileSystem() {
        networkService.sendCommand(new FileListMessage((UserSession) configurations.getProperty("session")));
    }

    private void createCommandResultHandler() {
        new Thread(() -> {
            while (true) {
                Object resultCommand = networkService.readCommandResult();
                if (resultCommand instanceof AbstractMessage) {
                    executor.execute((AbstractMessage) resultCommand);
                }
            }
        }).start();
    }

    public void shutdown() {
        networkService.closeConnection();
    }


}
