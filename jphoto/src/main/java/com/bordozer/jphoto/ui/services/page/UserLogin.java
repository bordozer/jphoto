package com.bordozer.jphoto.ui.services.page;

public class UserLogin {

    private final String name;
    private final String login;

    UserLogin(final String name, final String login) {
        this.name = name;
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("User %s, %s", login, name);
    }
}
