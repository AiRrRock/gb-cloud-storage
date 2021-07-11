package ru.aborichev.cloudstorage.server.service.impl;

import ru.aborichev.cloudstorage.core.session.AuthenticationStatus;
import ru.aborichev.cloudstorage.server.service.AuthenticationService;

public class EmptyAuthenticationService implements AuthenticationService {

    private static EmptyAuthenticationService instance;

    private EmptyAuthenticationService() {
    }

    public static EmptyAuthenticationService getInstance() {
        if (instance == null) {
            instance = new EmptyAuthenticationService();
        }
        return instance;
    }


    @Override
    public AuthenticationStatus authenticate(String userName, String password) {
        return AuthenticationStatus.SUCCESS;
    }
}
