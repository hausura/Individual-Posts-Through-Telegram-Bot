package com.io.project.config;

import com.io.project.model.User;

import java.io.*;
import java.util.*;

public class AllConfig {
    private List<String> whitelist = new ArrayList<>();
    private String password ;
    private Integer maxLogin;
    private Integer maxRequest;
    private String botName;
    private String botToken;
    private String geminiApiKey;
    private String splitSymbol;
    private String facebookApiUrl;
    private String facebookApiTarget;
    private String youtubeApi;
    private String articleApi;
    private String tiktokApi;

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

    public String getFacebookApiUrl() {
        return facebookApiUrl;
    }

    public String getFacebookApiTarget() {
        return facebookApiTarget;
    }

    public String getYoutubeApi() {
        return youtubeApi;
    }

    public String getArticleApi() {
        return articleApi;
    }

    public boolean isWhitelisted(Long id) {
        return whitelist.contains(id.toString());
    }

    public String geminiApiKey(){
        return this.geminiApiKey;
    }

    public String getSplitUrl() {
        return splitSymbol;
    }
    //Handle loader
    public void loadConfig() {
        List<String> whitelist = new ArrayList<>();
        Properties properties = new Properties();
        String password = "";
        String botToken = "";
        String botName = "";
        String geminiApiKey= "";
        Integer maxRequest = 0;
        Integer maxLogin = 0;
        String splitSymbol = "";
        String facebookApiUrl = "";
        String facebookApiTarget = "";
        String youtubeApi = "";
        String tiktokApi = "";
        String articleApi = "";

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
            geminiApiKey = properties.getProperty("geminiApiKey");
            splitSymbol = properties.getProperty("splitSymbol");
            facebookApiUrl = properties.getProperty("facebookApiUrl");
            facebookApiTarget = properties.getProperty("facebookApiTarget");
            youtubeApi = properties.getProperty("youtubeApi");
            tiktokApi = properties.getProperty("tiktokApi");
            articleApi = properties.getProperty("articleApi");

            // Khởi tạo user và password trong config
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
        this.geminiApiKey = geminiApiKey;
        this.splitSymbol = splitSymbol;
        this.facebookApiUrl = facebookApiUrl;
        this.facebookApiTarget = facebookApiTarget;
        this.youtubeApi = youtubeApi;
        this.tiktokApi = tiktokApi;
        this.articleApi = articleApi;
    }

    public void updateUserList(Long userId,User user) {
        this.listUser.put(userId, user);
    }
}
