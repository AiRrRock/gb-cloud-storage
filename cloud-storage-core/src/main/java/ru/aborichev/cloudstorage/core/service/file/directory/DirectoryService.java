package ru.aborichev.cloudstorage.core.service.file.directory;

import java.nio.file.Path;

public interface DirectoryService {
    void deleteDirectory(Path path);
    boolean createDirectory(Path path);
    boolean recreateDirectory(Path path);
}
