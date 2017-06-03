package model;


import model.message.*;

/**
 * Created by Alexander on 02/06/2017.
 */
public interface MessageHandler {
    void process(LastMessages message);

    void process(ListUsersResponse message);

    void process(LoginResponse message);

    void process(LogoutResponse message);

    void process(TextResponse message);

    void process(UserLoginMessage message);

    void process(UserLogoutMessage message);

    void process(UserMessage message);
}