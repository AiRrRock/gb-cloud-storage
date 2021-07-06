package ru.aborichev.view.treeitems;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import ru.aborichev.cloudstorage.core.model.FileNode;

public class RemoteFileNodeTreeItem extends TreeItem<FileNode> {
    private boolean requestChildren = true;

    public RemoteFileNodeTreeItem(FileNode node) {
        super(node);
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
        return getValue().isLeaf();
    }

    private ObservableList<TreeItem<FileNode>> buildChildren(TreeItem<FileNode> treeItem) {
        if (!getValue().isLeaf()) {
            ObservableList<TreeItem<FileNode>> children = FXCollections.observableArrayList();
            for (FileNode node : getValue().getChildren()) {
                children.add(new RemoteFileNodeTreeItem(node));
            }
            return children;
        }
        return FXCollections.emptyObservableList();
    }

}
