package ru.aborichev.service.file;

public interface FileSenderService {
    void sendFile(String localFilePath, String remoteFilePat);
}
