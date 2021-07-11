package ru.aborichev.service;

public interface FileSenderService {
    void sendFile(String localFilePath, String remoteFilePat);
}
