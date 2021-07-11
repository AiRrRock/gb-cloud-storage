package ru.aborichev.cloudstorage.core.service;

public interface FileIntegrityCheckerService {
    String calculateCheckSum(byte[] file);

    boolean compareCheckSum(String originCheckSum, String newCheckSum);

    boolean compareCheckSum(byte[] file, String originCheckSum);
}
