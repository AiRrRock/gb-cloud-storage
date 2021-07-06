package ru.aborichev.cloudstorage.core.messages.user;

import lombok.Getter;
import ru.aborichev.cloudstorage.core.session.UserSession;

import java.util.Base64;


@Getter
public class AuthenticationUserMessage extends AbstractUserMessage {
    private final String userName;
    private final String password;
    private UserSession session;

    public AuthenticationUserMessage(String userName, String password) {
        super();
        this.userName = Base64.getEncoder().encodeToString(userName.getBytes());
        this.password = Base64.getEncoder().encodeToString(password.getBytes());
    }

    public void setSession(UserSession session) {
        this.session = session;
    }
}
