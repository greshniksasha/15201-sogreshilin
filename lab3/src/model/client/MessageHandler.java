package model.client;


import model.message.*;

/**
 * Created by Alexander on 02/06/2017.
 */
public interface MessageHandler {

    void process(ListUsersSuccess message);

    void process(LoginSuccess message);

    void process(TextSuccess message);

    void process(UserLoginMessage message);

    void process(UserLogoutMessage message);

    void process(UserMessage message);

    void process(TextError textError);

    void process(LogoutSuccess logoutSuccess);

    void process(LogoutError logoutError);

    void process(LoginError loginError);

    void process(ListUsersError listUsersError);
}