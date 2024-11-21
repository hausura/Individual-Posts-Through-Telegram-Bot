package com.io.project.service;

import com.io.project.Utilities.PasswordUtility;
import com.io.project.config.AllConfig;
import com.io.project.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.io.project.Utilities.PasswordUtility.hashPassword;

public class AuthService {
    private final AllConfig allConfig;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    public AuthService(AllConfig allConfig){
        this.allConfig=allConfig;

    }

    public boolean authenticate(Long userId, String password) {
        Map<Long, String> userIdPassword = new HashMap<>();
        Map<Long, User> userList = allConfig.getListUser();
        logger.info("List user: {}",userList);
        for (Long userIdInConfig:userList.keySet()){
            userIdPassword.put(userIdInConfig, hashPassword(userList.get(userIdInConfig).getPassword()));
        }
        if (userIdPassword.containsKey(userId)) {
            String hashedPassword = userIdPassword.get(userId);
            return PasswordUtility.checkPassword(password, hashedPassword);
        }
        return false;
    }

    public void updateLoginStt(Long userId, Boolean status){
        User user = this.allConfig.getListUser().get(userId);
        if (user != null) {
            user.setLogin(status);
            this.allConfig.updateUserList(userId, user);
        } else {
            logger.error("User not found for ID: {}", userId);
        }
    }

    public boolean getLoginStatus(Long userId){
        User user = allConfig.getListUser().get(userId);
        return user.getLogin();
    }

    public boolean userInConfig(Long userId){
        allConfig.reloadWhiteList();
        return allConfig.isWhitelisted(userId);
    }
}