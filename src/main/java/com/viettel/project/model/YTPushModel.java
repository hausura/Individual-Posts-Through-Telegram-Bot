package com.viettel.project.model;

public class YTPushModel {
    Integer id = 0;
    String profileId;
    String profileType = "video";
    String action ="update_video_info";
    String url;

    public YTPushModel(Integer id, String profileId, String profileType, String action, String url) {
        this.id = id;
        this.profileId = profileId;
        this.profileType = profileType;
        this.action = action;
        this.url = url;
    }
}
