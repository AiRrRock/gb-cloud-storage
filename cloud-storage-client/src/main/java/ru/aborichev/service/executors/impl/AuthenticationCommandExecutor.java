package ru.aborichev.service.executors.impl;

import ru.aborichev.cloudstorage.core.messages.AbstractMessage;
import ru.aborichev.cloudstorage.core.messages.user.AuthenticationUserMessage;
import ru.aborichev.cloudstorage.core.service.environment.ConfigurationEnvironment;
import ru.aborichev.cloudstorage.core.service.executors.CommandExecutor;
import ru.aborichev.cloudstorage.core.session.UserSession;
import ru.aborichev.factory.Factory;

public class AuthenticationCommandExecutor implements CommandExecutor {

    private ConfigurationEnvironment environment;

    private static AuthenticationCommandExecutor instance;

    private AuthenticationCommandExecutor() {
        this.environment = Factory.getConfigurationEnvironment();
    }

    public static AuthenticationCommandExecutor getInstance() {
        if (instance == null) {
            instance = new AuthenticationCommandExecutor();
        }
        return instance;
    }

    @Override
    public void execute(AbstractMessage message) {
        UserSession session = null;
        if (message instanceof AuthenticationUserMessage) {
            session = ((AuthenticationUserMessage) message).getSession();
        }
        environment.setProperty("session", session);
    }
}

