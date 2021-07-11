package ru.aborichev.service;

import java.nio.file.Path;

public interface PathResolver {
    Path resolvePathToDownloadFile(String fileName);
}
