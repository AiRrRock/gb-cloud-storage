package ru.aborichev.cloudstorage.core.session.impl;

import lombok.Getter;
import lombok.Setter;
import ru.aborichev.cloudstorage.core.session.AuthenticationStatus;
import ru.aborichev.cloudstorage.core.session.UserSession;

import java.io.Serializable;


@Getter
@Setter
public class BasicUserSession implements UserSession, Serializable {
    private final String userName;
    private final String sessionId;
    private final AuthenticationStatus status;

    public BasicUserSession(String userName, String sessionId, AuthenticationStatus status) {
        this.userName = userName;
        this.sessionId = sessionId;
        this.status = status;
    }

}
