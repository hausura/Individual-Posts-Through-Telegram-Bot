package com.io.project.service;

import com.io.project.Utilities.PasswordUtility;
import com.io.project.config.AllConfig;
import com.io.project.model.User;

import java.util.HashMap;
import java.util.Map;

import static com.io.project.Utilities.PasswordUtility.hashPassword;

public class AuthService {
    private final AllConfig allConfig;
    private final Map<Long, String> userIdPassword = new HashMap<>();

    public AuthService(AllConfig allConfig){
        this.allConfig=allConfig;
        Map<Long, User> userList = allConfig.getListUser();
        for (Long userId:userList.keySet()){
            userIdPassword.put(userId, hashPassword(userList.get(userId).getPassword()));
        }
    }

    public boolean isWhitelisted(Long userId){
        return allConfig.isWhitelisted(userId);
    }

    public boolean authenticate(Long userId, String password) {
        if (userIdPassword.containsKey(userId)) {
            String hashedPassword = userIdPassword.get(userId);
            return PasswordUtility.checkPassword(password, hashedPassword);
        }
        return false;
    }

    public void updateLoginStt(Long userId, Boolean status){
        User user = allConfig.getListUser().get(userId);
        if (user != null) {
            user.setLogin(status);
            allConfig.updateUserList(userId, user);
        } else {
            System.out.println("User not found for ID: " + userId);
        }
    }

    public boolean getLoginStatus(Long userId){
        User user = allConfig.getListUser().get(userId);
        return user.getLogin();
    }
}