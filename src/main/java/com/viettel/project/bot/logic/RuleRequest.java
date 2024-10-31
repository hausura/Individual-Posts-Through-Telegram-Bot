package com.viettel.project.bot.logic;

import com.viettel.project.config.AllConfig;
import com.viettel.project.model.User;

public class RuleRequest {
    private AllConfig allConfig;
    public RuleRequest(AllConfig allConfig){
        this.allConfig=allConfig;
    }

    public Integer checkNumPush(Long userId){
        User user = allConfig.getListUser().get(userId);
        Integer numRequestPush = user.getNumRequest();
        if(numRequestPush> allConfig.getMaxRequest()) return 0;
        return 1 ;
    }

    public Integer checkNumLogin(Long userId){
        User user = allConfig.getListUser().get(userId);
        Integer numRequestPush = user.getNumLogin();
        if(numRequestPush> allConfig.getMaxLogin()) return 0;
        return 1 ;
    }

    public void updateNumPush(Long userId){
        User user = allConfig.getListUser().get(userId);
        if (user != null) {
            Integer numRequestPush = user.getNumRequest();
            user.setNumRequest(numRequestPush + 1);
            System.out.println("numRequest of user now: " + user.getNumRequest());
            allConfig.updateUserList(userId, user);
        } else {
            System.out.println("User not found for ID: " + userId);
        }
    }

    public void updateNumLogin(Long userId){
        User user = allConfig.getListUser().get(userId);
        if (user != null) {
            Integer numLogin = user.getNumLogin();
            user.setNumLogin(numLogin + 1);
            System.out.println("numLogin "+numLogin);
            System.out.println("NumLogin of" +userId+"now: " + user.getNumLogin());
            allConfig.updateUserList(userId, user);
        } else {
            System.out.println("User not found for ID: " + userId);
        }
    }
}
