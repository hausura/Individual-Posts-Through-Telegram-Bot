package com.io.project.model;

public class JobModel {
    private int id;
    private String profileId;
    private String profileType;
    private String action;
    private String url;

    public JobModel(String url) {
        this.url = url;
    }

    public JobModel(String profileId, String profileType, String action) {
        this.profileId = profileId;
        this.profileType = profileType;
        this.action = action;
    }
    public JobModel(String profileId, String profileType, String action, String url) {
        this.profileId = profileId;
        this.profileType = profileType;
        this.action = action;
        this.url = url;
    }
}