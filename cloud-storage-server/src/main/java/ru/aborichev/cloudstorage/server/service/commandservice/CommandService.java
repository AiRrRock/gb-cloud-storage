package ru.aborichev.cloudstorage.server.service.commandservice;

public interface CommandService {
    String processCommand(String command);
    String getCommand();
}
