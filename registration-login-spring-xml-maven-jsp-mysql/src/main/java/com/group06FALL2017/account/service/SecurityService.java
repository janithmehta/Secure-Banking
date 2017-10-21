package com.group06FALL2017.account.service;

public interface SecurityService {
    String findLoggedInUsername();

    void autologin(String username, String password);
}
