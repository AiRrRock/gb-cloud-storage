package ru.aborichev.cloudstorage.core.service;

import java.nio.file.Path;

public interface CompressionService {
    Path compress(Path path);

    void decompress(Path archive, String originalName);
}
