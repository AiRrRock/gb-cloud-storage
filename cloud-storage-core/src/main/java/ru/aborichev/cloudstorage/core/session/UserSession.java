package ru.aborichev.cloudstorage.core.session;

public interface UserSession {
    String getSessionId();
    String getUserName();

    AuthenticationStatus getStatus();

}
