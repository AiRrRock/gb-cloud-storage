package ru.aborichev.cloudstorage.core.service;

import java.nio.file.Path;

public interface DirectoryService {
    void deleteDirectory(Path path);
}
