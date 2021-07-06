package ru.aborichev.factory;

import ru.aborichev.cloudstorage.core.service.compression.CompressionService;
import ru.aborichev.cloudstorage.core.service.compression.impl.ZipCompressionService;
import ru.aborichev.cloudstorage.core.service.environment.impl.BasicConfigurationEnvironment;
import ru.aborichev.cloudstorage.core.service.file.chunks.FileChunkProviderService;
import ru.aborichev.cloudstorage.core.service.file.chunks.impl.BasicFileChunkProviderService;
import ru.aborichev.cloudstorage.core.service.file.directory.DirectoryService;
import ru.aborichev.cloudstorage.core.service.file.directory.impl.BasicDirectoryService;
import ru.aborichev.cloudstorage.core.service.file.integrity.FileIntegrityCheckerService;
import ru.aborichev.cloudstorage.core.service.file.integrity.impl.MD5FileIntegrityCheckerService;
import ru.aborichev.cloudstorage.core.service.providers.CommandExecutorProviderService;
import ru.aborichev.cloudstorage.core.service.environment.ConfigurationEnvironment;
import ru.aborichev.service.file.FileSenderService;
import ru.aborichev.service.file.impl.BasicFileSenderService;
import ru.aborichev.service.resolver.impl.LocalFilePathResolver;
import ru.aborichev.service.resolver.PathResolver;
import ru.aborichev.service.network.NetworkService;
import ru.aborichev.service.providers.impl.BasicCommandExecutorServiceProvider;
import ru.aborichev.service.network.impl.IONetworkService;

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