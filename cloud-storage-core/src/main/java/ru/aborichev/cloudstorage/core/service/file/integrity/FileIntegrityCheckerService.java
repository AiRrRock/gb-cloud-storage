package ru.aborichev.cloudstorage.core.service.file.integrity;

public interface FileIntegrityCheckerService {
    String calculateCheckSum(byte[] file);

    boolean compareCheckSum(String originCheckSum, String newCheckSum);

    boolean compareCheckSum(byte[] file, String originCheckSum);
}
