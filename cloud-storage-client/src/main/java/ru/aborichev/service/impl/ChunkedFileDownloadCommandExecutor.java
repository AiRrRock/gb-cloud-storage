package ru.aborichev.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.aborichev.cloudstorage.core.messages.AbstractMessage;
import ru.aborichev.cloudstorage.core.messages.file.ChunkedFileDownloadMessage;
import ru.aborichev.cloudstorage.core.model.Chunk;
import ru.aborichev.cloudstorage.core.model.ChunkedFile;
import ru.aborichev.cloudstorage.core.service.CommandExecutor;
import ru.aborichev.cloudstorage.core.session.UserSession;
import ru.aborichev.cloudstorage.core.service.FileIntegrityCheckerService;
import ru.aborichev.factory.Factory;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class ChunkedFileDownloadCommandExecutor implements CommandExecutor {
    private static ChunkedFileDownloadCommandExecutor instance;
    private static final Logger LOGGER = LogManager.getLogger(ChunkedFileDownloadCommandExecutor.class);
    private final FileIntegrityCheckerService integrityChecker;
    private final Map<String, ChunkedFile> chunkedFiles;

    private ChunkedFileDownloadCommandExecutor() {
        this.chunkedFiles = new HashMap<>();
        this.integrityChecker = Factory.getFileIntegrityChecker();
    }

    public static ChunkedFileDownloadCommandExecutor getInstance() {
        if (instance == null) {
            instance = new ChunkedFileDownloadCommandExecutor();
        }
        return instance;
    }

    @Override
    public void execute(AbstractMessage message) {
        ChunkedFileDownloadMessage chunkedFileMessage = null;
        LOGGER.debug("Got message of type " + message.getClass());
        if (message instanceof ChunkedFileDownloadMessage) {
            LOGGER.info("Got message of type " + message.getClass());
            chunkedFileMessage = (ChunkedFileDownloadMessage) message;
            String fileName = chunkedFileMessage.getFileName();
            String id = chunkedFileMessage.getDownloadID();

            ChunkedFileWriter writer = null;

            ChunkedFile chunkedFile = chunkedFiles.get(id);
            if (chunkedFile == null) {
                chunkedFile = new ChunkedFile(chunkedFileMessage.getNumberOfChunks(), fileName);//, chunkedFileMessage.getServerFilePath());

                Path filePath = Factory.getPathResolver().resolvePathToDownloadFile(fileName);
                chunkedFile.setPathToResultingFile(filePath);
                chunkedFile.setDownloadID(id);
                chunkedFile.setCompressed(chunkedFileMessage.isCompressed());
                chunkedFile.setOriginalName(chunkedFileMessage.getOriginalName());
                chunkedFile.setRemoteFilePath(chunkedFileMessage.getRelativeFilePath());

                try {
                    Files.deleteIfExists(filePath);
                    Factory.getDirectoryService().deleteDirectory(Paths.get(filePath.getParent().toAbsolutePath().toString(), chunkedFileMessage.getOriginalName()));
                    Files.createFile(filePath);
                } catch (IOException e) {
                    LOGGER.error(e);
                    return;
                }

                writer = new ChunkedFileWriter(chunkedFile);
                chunkedFiles.put(id, chunkedFile);

            }

            Chunk chunk = chunkedFileMessage.getCurrentChunk();
            chunkedFile.addChunk(chunkedFileMessage.getChunkNumber(), chunk);

            LOGGER.info(String.format("Received file chunk N%d of [%d] chunks for file [%s]", chunk.getChunkNumber(), chunkedFile.getNumberOfChunks(), chunkedFile.getPathToResultingFile().toString()));

            if (writer != null) {
                (new Thread(writer)).start();
            }

        }


    }

    private class ChunkedFileWriter implements Runnable {
        private ChunkedFile file;

        public ChunkedFileWriter(ChunkedFile file) {
            this.file = file;
        }

        @Override
        public void run() {
            LOGGER.info(String.format("Started asynchronous download of file [%s]", file.getPathToResultingFile()));
            while (!file.isCompleted()) {
                if (file.isReadyToWriteNextChunk()) {
                    try {
                        Chunk chunk = file.getChunk();
                        String md5 = integrityChecker.calculateCheckSum(chunk.getData());
                        if (integrityChecker.compareCheckSum(chunk.getMd5(), md5)) {
                            LOGGER.debug("Chunk md5 is " + md5);
                            byte[] chunkToWrite = new byte[chunk.getSize()];
                            System.arraycopy(chunk.getData(), 0, chunkToWrite, 0, chunk.getSize());
                            Files.write(file.getPathToResultingFile(), chunkToWrite, StandardOpenOption.APPEND);
                            if (file.isCompleted()) {
                                break;
                            } else {
                                file.setCurrentChunk(file.getCurrentChunk() + 1);
                            }
                        } else {
                            LOGGER.info(String.format("Chunk [file = %s, chunk number = %d] integrity seems to be compromised, the chunk will be re-downloaded", file.getFileName(), chunk.getChunkNumber()));
                        }
                        Factory.getNetworkService().sendCommand(new ChunkedFileDownloadMessage((UserSession) Factory.getConfigurationEnvironment().getProperty("session"), file.getFileName(), file.getRemoteFilePath(), file.getDownloadID(), file.getCurrentChunk()));
                    } catch (IOException e) {
                        LOGGER.error(e);
                    }
                }
            }

            if (file.isCompressed()) {
                Factory.getCompressionService().decompress(file.getPathToResultingFile(), file.getOriginalName());
                try {
                    Files.deleteIfExists(file.getPathToResultingFile());
                } catch (IOException e) {
                    LOGGER.error(e);
                }
            }

            chunkedFiles.remove(file.getDownloadID());

            LOGGER.info(String.format("Finished file download for %s", file.getPathToResultingFile().toFile().getAbsolutePath()));

        }
    }

}
