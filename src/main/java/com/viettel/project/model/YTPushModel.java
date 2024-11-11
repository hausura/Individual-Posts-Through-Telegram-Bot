package com.viettel.project.model;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YTPushModel {
    Integer id = 0;
    String profileId;
    String profileType = "video";
    String action ="update_video_info";
    String url;

    public YTPushModel(String url) {
        this.profileId = getVideoId(url);
        this.url = url;
    }

    @Override
    public String toString() {
        return "YTPushModel{" +
                "id=" + id +
                ", profileId='" + profileId + '\'' +
                ", profileType='" + profileType + '\'' +
                ", action='" + action + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public static String getVideoId(String url) {
        String videoId = null;
        String regex = "(?<=v=|/videos/|embed/|youtu.be/|/v/|/e/|watch\\?v=|&v=|\\?v=|/watch\\?v=|&v=|/v=|/\\d/|/vi/|watch\\?vi=|/videos/|embed/|youtu.be/|/v/|\\u0026v=|/e/|watch\\?v=|&v=|\\u0026vi=|/embed/|/vi/|/v=|watch\\?vi=|watch\\?v=|/v=|youtu.be/|/embed/|/vi/|watch\\?v=|/videos/|embed/|youtu.be/|/v=|/e/|watch\\?v=|&v=|\\?v=|/vi/|/v=|embed/|watch\\?v=|watch\\?vi=|watch\\?v=|&v=)([a-zA-Z0-9_-]{11})";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            videoId = matcher.group(1);
        }
        return videoId;
    }
}
