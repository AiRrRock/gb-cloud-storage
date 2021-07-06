package ru.aborichev.cloudstorage.core.service.file.directory.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.aborichev.cloudstorage.core.service.file.chunks.impl.BasicFileChunkProviderService;
import ru.aborichev.cloudstorage.core.service.file.directory.DirectoryService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class BasicDirectoryService implements DirectoryService {
    private static final Logger LOGGER = LogManager.getLogger(BasicDirectoryService.class);
    private static BasicDirectoryService instance;

    private BasicDirectoryService() {

    }

    public static BasicDirectoryService getInstance() {
        if (instance == null) {
            instance = new BasicDirectoryService();
        }
        return instance;
    }


    @Override
    public void deleteDirectory(Path path) {
        try {
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    @Override
    public boolean createDirectory(Path path) {
        return false;
    }

    @Override
    public boolean recreateDirectory(Path path) {
        return false;
    }
}
