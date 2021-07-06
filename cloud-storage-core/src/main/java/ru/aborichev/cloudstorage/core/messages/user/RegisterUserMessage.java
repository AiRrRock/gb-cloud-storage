package ru.aborichev.cloudstorage.core.messages.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserMessage extends AuthenticationUserMessage {
    private String errorMessage;
    public RegisterUserMessage(String userName, String password) {
        super(userName, password);
    }
}
