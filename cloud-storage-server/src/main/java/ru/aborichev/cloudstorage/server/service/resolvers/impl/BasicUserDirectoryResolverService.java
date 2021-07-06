package ru.aborichev.cloudstorage.server.service.resolvers.impl;

import ru.aborichev.cloudstorage.core.service.environment.ConfigurationEnvironment;
import ru.aborichev.cloudstorage.server.factory.Factory;
import ru.aborichev.cloudstorage.server.service.resolvers.UserDirectoryResolverService;

import java.util.Base64;

public class BasicUserDirectoryResolverService implements UserDirectoryResolverService {
    private static final ConfigurationEnvironment configurations = Factory.getConfigurationEnvironment();
    private static BasicUserDirectoryResolverService instance;

    private BasicUserDirectoryResolverService() {
    }

    public static BasicUserDirectoryResolverService getInstance() {
        if (instance == null) {
            instance = new BasicUserDirectoryResolverService();
        }
        return instance;
    }

    @Override
    public String resolveByName(String userName) {
        return (String) configurations.getProperty("baseDir") + "/" + new String(Base64.getDecoder().decode(userName.getBytes()));
    }

    @Override
    public java.lang.String resolveBySessionID(java.lang.String sessionID) {
        return null;
    }
}
