package ru.aborichev.cloudstorage.core.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class FileNode implements Serializable {
    private String name;
    private String filePath;
    private List<FileNode> children;
    private boolean leaf;

    public FileNode(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return name == null || name.isEmpty() ? filePath : name;
    }
}
