package ru.aborichev.cloudstorage.server.service.resolvers;

public interface UserDirectoryResolverService {
    String resolveByName(String userName);
    String resolveBySessionID(String sessionID);
}
