package com.viettel.project.config;

import com.viettel.project.model.User;

import java.io.*;
import java.util.*;

public class AllConfig {
    private List<String> whitelist = new ArrayList<>();
    private String password ;
    private Integer maxLogin;
    private Integer maxRequest;
    private String botName;
    private String botToken;
    private Map<Long, User> listUser = new HashMap<>();

    // Get data from config
    public Map<Long, User> getListUser() {
        return listUser;
    }

    public String getBotName() {
        return botName;
    }

    public String getBotToken() {
        return botToken;
    }

    public Integer getMaxRequest() {
        return this.maxRequest;
    }

    public Integer getMaxLogin() {
        return this.maxLogin;
    }

    public boolean isWhitelisted(Long id) {
        return whitelist.contains(id.toString());
    }

    //Handle loader
    public void loadConfig() {
        List<String> whitelist = new ArrayList<>();
        Properties properties = new Properties();
        String password = "";
        String botToken = "";
        String botName = "";
        Integer maxRequest = 0;
        Integer maxLogin = 0;
        try (BufferedReader input =new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/config/config.properties")))) {
            // Tải file properties
            properties.load(input);
            // Lấy danh sách whitelist và chia tách thành mảng
            String whitelistString = properties.getProperty("whitelist");
            password = properties.getProperty("password");
            maxRequest = Integer.parseInt(properties.getProperty("maxRequest"));
            maxLogin = Integer.parseInt(properties.getProperty("maxLogin"));
            botToken = properties.getProperty("botToken");
            botName = properties.getProperty("botName");
            //System.out.println("whitelistString: "+whitelistString);
            if (!whitelistString.strip().isEmpty()) {
                String[] whitelistArray = whitelistString.split(",");
                whitelist.addAll(Arrays.asList(whitelistArray));
                for(String userId: whitelist){
                    User user = new User(Long.parseLong(userId), password,0,0,false);
                    listUser.put(user.getUserId(), user);
                }
            }
            System.out.println(whitelist);
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc file: " + e.getMessage());
        }
        this.whitelist = whitelist;
        this.password = password;
        this.maxRequest = maxRequest;
        this.maxLogin = maxLogin;
        this.botName = botName;
        this.botToken = botToken;
    }

    public void updateUserList(Long userId,User user){
        this.listUser.put(userId,user);
    }

    public static void main(String[] args) {
        AllConfig allConfig =new AllConfig();
        allConfig.loadConfig();
        System.out.println(allConfig.getBotName());
    }
}
