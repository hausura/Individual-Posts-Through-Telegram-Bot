package com.io.project.config;

import com.io.project.model.User;
import lombok.Getter;

import java.io.*;
import java.util.*;

public class AllConfig {
    private List<String> whitelist = new ArrayList<>();
    @Getter
    private Integer maxLogin;
    @Getter
    private Integer maxRequest;
    @Getter
    private String botName;
    @Getter
    private String botToken;
    private String geminiApiKey;
    private String splitSymbol;
    @Getter
    private String facebookApiUrl;
    @Getter
    private String facebookApiTarget;
    @Getter
    private String youtubeApi;
    @Getter
    private String articleApi;
    @Getter
    private String tiktokApi;

    // Get data from config
    @Getter
    private final Map<Long, User> listUser = new HashMap<>();

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
        String password;
        String botToken = "";
        String botName = "";
        String geminiApiKey= "";
        int maxRequest = 0;
        int maxLogin = 0;
        String splitSymbol = "";
        String facebookApiUrl = "";
        String facebookApiTarget = "";
        String youtubeApi = "";
        String tiktokApi = "";
        String articleApi = "";

        try (BufferedReader input =new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/config/config.properties"))))) {
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
            if (!whitelistString.isBlank()) {
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
