package ru.aborichev.cloudstorage.server.factory;

import ru.aborichev.cloudstorage.core.service.compression.CompressionService;
import ru.aborichev.cloudstorage.core.service.compression.impl.ZipCompressionService;
import ru.aborichev.cloudstorage.core.service.environment.ConfigurationEnvironment;
import ru.aborichev.cloudstorage.core.service.environment.impl.BasicConfigurationEnvironment;
import ru.aborichev.cloudstorage.core.service.file.chunks.FileChunkProviderService;
import ru.aborichev.cloudstorage.core.service.file.chunks.impl.BasicFileChunkProviderService;
import ru.aborichev.cloudstorage.core.service.file.directory.DirectoryService;
import ru.aborichev.cloudstorage.core.service.file.directory.impl.BasicDirectoryService;
import ru.aborichev.cloudstorage.core.service.file.integrity.FileIntegrityCheckerService;
import ru.aborichev.cloudstorage.core.service.file.integrity.impl.MD5FileIntegrityCheckerService;
import ru.aborichev.cloudstorage.core.service.providers.CommandExecutorProviderService;
import ru.aborichev.cloudstorage.server.service.authentication.AuthenticationService;
import ru.aborichev.cloudstorage.server.service.commandservice.CommandDictionaryService;
import ru.aborichev.cloudstorage.server.service.commandservice.CommandService;
import ru.aborichev.cloudstorage.server.service.commandservice.CommandDictionaryServiceImpl;
import ru.aborichev.cloudstorage.server.service.authentication.impl.EmptyAuthenticationService;
import ru.aborichev.cloudstorage.server.service.network.impl.IOClientService;
import ru.aborichev.cloudstorage.server.service.network.impl.NettyServerService;
import ru.aborichev.cloudstorage.server.service.network.ClientService;
import ru.aborichev.cloudstorage.server.service.network.ServerService;
import ru.aborichev.cloudstorage.server.service.providers.BasicCommandExecutorProviderService;
import ru.aborichev.cloudstorage.server.service.resolvers.impl.BasicUserDirectoryResolverService;
import ru.aborichev.cloudstorage.server.service.resolvers.UserDirectoryResolverService;

import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class Factory {

    public static ServerService getServerService() {
        return NettyServerService.getInstance();
    }

    public static ClientService getClientService(Socket socket) {
        return new IOClientService(socket);
    }

    public static CommandDictionaryService getCommandDirectoryService() {
        return new CommandDictionaryServiceImpl();
    }

    @Deprecated
    public static List<CommandService> getCommandServices() {
        return Arrays.asList();
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
