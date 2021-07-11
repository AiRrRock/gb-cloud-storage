package ru.aborichev.factory;

import ru.aborichev.cloudstorage.core.service.CompressionService;
import ru.aborichev.cloudstorage.core.service.impl.ZipCompressionService;
import ru.aborichev.cloudstorage.core.service.impl.BasicConfigurationEnvironment;
import ru.aborichev.cloudstorage.core.service.FileChunkProviderService;
import ru.aborichev.cloudstorage.core.service.impl.BasicFileChunkProviderService;
import ru.aborichev.cloudstorage.core.service.DirectoryService;
import ru.aborichev.cloudstorage.core.service.impl.BasicDirectoryService;
import ru.aborichev.cloudstorage.core.service.FileIntegrityCheckerService;
import ru.aborichev.cloudstorage.core.service.impl.MD5FileIntegrityCheckerService;
import ru.aborichev.cloudstorage.core.service.CommandExecutorProviderService;
import ru.aborichev.cloudstorage.core.service.ConfigurationEnvironment;
import ru.aborichev.service.FileSenderService;
import ru.aborichev.service.impl.BasicFileSenderService;
import ru.aborichev.service.impl.LocalFilePathResolver;
import ru.aborichev.service.PathResolver;
import ru.aborichev.service.NetworkService;
import ru.aborichev.service.impl.BasicCommandExecutorServiceProvider;
import ru.aborichev.service.impl.IONetworkService;

public class Factory {

    public static NetworkService getNetworkService() {
        return IONetworkService.getInstance();
    }

    public static CommandExecutorProviderService getCommandExecutorProvider() {
        return BasicCommandExecutorServiceProvider.getInstance();
    }

    public static ConfigurationEnvironment getConfigurationEnvironment() {
        return BasicConfigurationEnvironment.getInstance();
    }

    public static PathResolver getPathResolver() {
        return LocalFilePathResolver.getInstance();
    }

    public static FileIntegrityCheckerService getFileIntegrityChecker() {
        return MD5FileIntegrityCheckerService.getInstance();
    }

    public static CompressionService getCompressionService(){
        return ZipCompressionService.getInstance();
    }

    public static FileSenderService getFileSenderService(){
        return BasicFileSenderService.getInstance();
    }

    public static FileChunkProviderService getFileChunkProvider(){
        return BasicFileChunkProviderService.getInstance();
    }

    public static DirectoryService getDirectoryService() {
        return BasicDirectoryService.getInstance();
    }

}