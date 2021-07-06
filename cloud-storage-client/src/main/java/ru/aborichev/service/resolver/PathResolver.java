package ru.aborichev.service.resolver;

import java.nio.file.Path;

public interface PathResolver {
    Path resolvePathToSelectedFolder();
    Path resolvePathToDownloadFile(String fileName);
}
