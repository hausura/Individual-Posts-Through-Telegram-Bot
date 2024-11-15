package com.io.project.config;

import com.io.project.model.User;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class AllConfig {
    private static final Logger logger = LoggerFactory.getLogger(AllConfig.class);

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
    // API URL
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

    // Pattern
    @Getter
    public String FB_URL_REGEX;
    @Getter
    public String FB_ID_REGEX;
    @Getter
    public String YOUTUBE_REGEX;
    @Getter
    public String TIKTOK_REGEX;
    @Getter
    public String ARTICLE_REGEX;

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
        String FB_URL_REGEX = "";
        String FB_ID_REGEX = "";
        String YOUTUBE_REGEX = "";
        String TIKTOK_REGEX = "";
        String ARTICLE_REGEX = "";

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config/config.properties")) {
            if (input == null) {
                logger.error("Property file not found");
            }            // Tải file properties
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
            FB_URL_REGEX = properties.getProperty("FB_URL_REGEX");
            FB_ID_REGEX = properties.getProperty("FB_ID_REGEX");
            YOUTUBE_REGEX = properties.getProperty("YOUTUBE_REGEX");
            TIKTOK_REGEX = properties.getProperty("TIKTOK_REGEX");
            ARTICLE_REGEX = properties.getProperty("ARTICLE_REGEX");

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
            logger.error("Lỗi khi đọc file: {}", e.getMessage());
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
        this.FB_URL_REGEX = FB_URL_REGEX;
        this.FB_ID_REGEX = FB_ID_REGEX;
        this.YOUTUBE_REGEX = YOUTUBE_REGEX;
        this.TIKTOK_REGEX = TIKTOK_REGEX;
        this.ARTICLE_REGEX = ARTICLE_REGEX;
    }

    public void updateUserList(Long userId,User user) {
        this.listUser.put(userId, user);
    }
}
