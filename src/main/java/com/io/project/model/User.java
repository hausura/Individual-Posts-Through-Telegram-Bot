package com.io.project.model;

import lombok.Getter;
import lombok.Setter;

public class User {
    @Setter
    @Getter
    Long userId;
    @Setter
    @Getter
    String password;
    @Setter
    @Getter
    Integer numRequest;
    @Setter
    @Getter
    Integer numLogin;
    Boolean isLogin;
    public User(Long userId, String password,Integer numRequest,Integer numLogin,Boolean isLogin){
        this.userId=userId;
        this.password=password;
        this.numRequest=numRequest;
        this.numLogin=numLogin;
        this.isLogin=isLogin;
    }

    public Boolean getLogin() {
        return isLogin;
    }

    public void setLogin(Boolean login) {
        isLogin = login;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", password='" + password + '\'' +
                ", numRequest=" + numRequest +
                '}';
    }
}
