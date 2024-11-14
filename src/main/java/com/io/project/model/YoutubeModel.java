package com.io.project.model;


import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class YoutubeModel {
    private final int id = 0;
    private final String profileId;
    private final String profileType = "video";
    private final String action;
    private final String url;

    public YoutubeModel(String url,String action) {
        this.profileId = getVideoId(url);
        this.url = url;
        this.action = action;
    }

    @Override
    public String toString() {
        return "YoutubeModel{" +
                "id=" + id +
                ", profileId='" + profileId + '\'' +
                ", profileType='" + profileType + '\'' +
                ", action='" + action + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public static String getVideoId(String url) {
        String videoId = null;
        String regex = "(?<=v=|/videos/|embed/|youtu.be/|/v/|/e/|watch\\?v=|&v=|\\?v=|/watch\\?v=|&v=|/v=|/\\d/|/vi/|watch\\?vi=|/embed/|/vi/|/v=|watch\\?vi=|/v=|/embed/|/vi/|watch\\?v=|/videos/|embed/|youtu.be/|/v=|/e/|watch\\?v=|&v=|\\?v=|/vi/|/v=|embed/|watch\\?v=|watch\\?vi=|watch\\?v=|&v=)([a-zA-Z0-9_-]{11})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            videoId = matcher.group(1);
        }
        return videoId;
    }
}
