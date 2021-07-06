package ru.aborichev.view.treeitems;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import ru.aborichev.cloudstorage.core.model.FileNode;

public class LocalFileNodeTreeItem extends TreeItem<FileNode> {
    private boolean requestChildren = true;
    private boolean canBeLeaf = true;
    private boolean isLeaf;

    public LocalFileNodeTreeItem(String fileName, String filePath) {
        super(new FileNode(fileName, filePath));
        this.expandedProperty().addListener((observable, oldValue, newValue) -> requestChildren = true);
    }

    @Override
    public ObservableList<TreeItem<FileNode>> getChildren() {
        if (requestChildren) {
            requestChildren = false;
            super.getChildren().setAll(buildChildren(this));
        }
        return super.getChildren();
    }

    @Override
    public boolean isLeaf() {
        if (canBeLeaf) {
            canBeLeaf = false;
            File f = new File(getValue().getFilePath());
            isLeaf = f.isFile();
        }
        return isLeaf;
    }

    private ObservableList<TreeItem<FileNode>> buildChildren(TreeItem<FileNode> treeItem) {
        File f = new File(String.valueOf(treeItem.getValue().getFilePath()));
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            if (files != null) {
                ObservableList<TreeItem<FileNode>> children = FXCollections
                        .observableArrayList();

                for (File childFile : files) {
                    children.add(new LocalFileNodeTreeItem(childFile.getName(), childFile.getAbsolutePath()));
                }

                return children;
            }
        }

        return FXCollections.emptyObservableList();
    }


}