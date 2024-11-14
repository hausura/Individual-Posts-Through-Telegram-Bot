package com.io.project.config;

public class Define {
    // Command
    public static final String FB_ID_COMMAND = "/add_facebook_target_id";
    public static final String FB_URL_COMMAND = "/add_facebook_url";
    public static final String YT_COMMAND = "/add_youtube";
    public static final String TIKTOK_COMMAND = "/add_tiktok";
    public static final String AT_COMMAND = "/add_article";

    // Pattern
    public static final String FB_URL_REGEX = "^https://www\\.facebook\\.com/\\w+/posts/[\\w-]+\\??.*$";
    public static final String FB_ID_REGEX = "([A-Za-z0-9_]+)_([A-Za-z0-9_]+)";
    public static final String YOUTUBE_REGEX = "^https://www\\.youtube\\.com/watch\\?v=[\\w-]+(&.*)?$";
    public static final String TIKTOK_REGEX = "^https://www\\.tiktok\\.com/@[\\w.]+/video/\\d+$";
    public static final String ARTICLE_REGEX = "^https://.*$";
}
