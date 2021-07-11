package ru.aborichev.cloudstorage.server.service;

public interface UserDirectoryResolverService {
    String resolveByName(String userName);
    String resolveBySessionID(String sessionID);
}
