package ru.aborichev.cloudstorage.server.service.authentication;

import ru.aborichev.cloudstorage.core.session.AuthenticationStatus;

public interface AuthenticationService {
    AuthenticationStatus authenticate(String userName, String password);
}
