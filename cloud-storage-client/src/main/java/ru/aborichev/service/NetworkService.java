package ru.aborichev.service;

import ru.aborichev.cloudstorage.core.messages.AbstractMessage;

public interface NetworkService {

    void sendCommand(AbstractMessage command);

    Object readCommandResult();

    void closeConnection();

}