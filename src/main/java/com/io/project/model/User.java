package com.io.project.model;

public class User {
    Long userId;
    String password;
    Integer numRequest;
    Integer numLogin;
    Boolean isLogin;
    public User(Long userId, String password,Integer numRequest,Integer numLogin,Boolean isLogin){
        this.userId=userId;
        this.password=password;
        this.numRequest=numRequest;
        this.numLogin=numLogin;
        this.isLogin=isLogin;
    }

    public Integer getNumLogin() {
        return numLogin;
    }

    public void setNumLogin(Integer numLogin) {
        this.numLogin = numLogin;
    }

    public Integer getNumRequest() {
        return numRequest;
    }

    public Long getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getLogin() {
        return isLogin;
    }

    public void setNumRequest(Integer numRequest) {
        this.numRequest = numRequest;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
