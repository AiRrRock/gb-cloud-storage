package ru.aborichev.cloudstorage.server.factory;

import ru.aborichev.cloudstorage.core.service.CompressionService;
import ru.aborichev.cloudstorage.core.service.impl.ZipCompressionService;
import ru.aborichev.cloudstorage.core.service.ConfigurationEnvironment;
import ru.aborichev.cloudstorage.core.service.impl.BasicConfigurationEnvironment;
import ru.aborichev.cloudstorage.core.service.FileChunkProviderService;
import ru.aborichev.cloudstorage.core.service.impl.BasicFileChunkProviderService;
import ru.aborichev.cloudstorage.core.service.DirectoryService;
import ru.aborichev.cloudstorage.core.service.impl.BasicDirectoryService;
import ru.aborichev.cloudstorage.core.service.FileIntegrityCheckerService;
import ru.aborichev.cloudstorage.core.service.impl.MD5FileIntegrityCheckerService;
import ru.aborichev.cloudstorage.core.service.CommandExecutorProviderService;
import ru.aborichev.cloudstorage.server.service.AuthenticationService;
import ru.aborichev.cloudstorage.server.service.impl.EmptyAuthenticationService;
import ru.aborichev.cloudstorage.server.service.impl.NettyServerService;
import ru.aborichev.cloudstorage.server.service.ServerService;
import ru.aborichev.cloudstorage.server.service.impl.BasicCommandExecutorProviderService;
import ru.aborichev.cloudstorage.server.service.impl.BasicUserDirectoryResolverService;
import ru.aborichev.cloudstorage.server.service.UserDirectoryResolverService;

public class Factory {

    public static ServerService getServerService() {
        return NettyServerService.getInstance();
    }

    public static ConfigurationEnvironment getConfigurationEnvironment() {
        return BasicConfigurationEnvironment.getInstance();
    }

    public static CommandExecutorProviderService getCommandExecutorProvider() {
        return BasicCommandExecutorProviderService.getInstance();
    }

    public static UserDirectoryResolverService getUserDirectoryResolver() {
        return BasicUserDirectoryResolverService.getInstance();
    }

    public static AuthenticationService getAuthenticationService() {
        return EmptyAuthenticationService.getInstance();
    }

    public static FileIntegrityCheckerService getFileIntegrityChecker() {
        return MD5FileIntegrityCheckerService.getInstance();
    }

    public static FileChunkProviderService getFileChunkProvider() {
        return BasicFileChunkProviderService.getInstance();
    }

    public static CompressionService getCompressionService() {
        return ZipCompressionService.getInstance();
    }

    public static DirectoryService getDirectoryService() {
        return BasicDirectoryService.getInstance();
    }


}
