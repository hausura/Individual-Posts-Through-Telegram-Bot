package com.io.project.model;

import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class TiktokModel {
    private final int id = 0;
    private String profileId;
    private String profileType = "video";
    private final String action;
    private final String url;

    public TiktokModel(String url,String action) {
        this.profileId = getTikTokVideoId(url);
        this.url = url;
        this.action = action;
    }

    @Override
    public String toString() {
        return "TiktokModel{" +
                "id=" + id +
                ", profileId='" + profileId + '\'' +
                ", profileType='" + profileType + '\'' +
                ", action='" + action + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public static String getTikTokVideoId(String url) {

        String regex = "https?://(?:www\\.)?tiktok\\.com/(?:@[\\w.-]+/)?video/(\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
