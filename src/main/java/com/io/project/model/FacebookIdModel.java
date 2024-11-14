package com.io.project.model;

import lombok.Getter;

@Getter
public class FacebookIdModel {
    Integer id= 0;
    String target_id; //716550913529554_886364173253325",
    String profile_type = "USER";
    String job_action = "QUERY_SINGLE_POST";
    String job_output= null;
    String time_start = null;
    String time_stop = null;
    String token_action = null;
    String object_depend = null;
    String priority = "-";

    public FacebookIdModel(String target_id) {
        this.target_id = target_id;
    }

    public FacebookIdModel(Integer id, String target_id, String profile_type, String job_action, String job_output, String time_start, String time_stop, String token_action, String object_depend, String priority) {
        this.id = id;
        this.target_id = target_id;
        this.profile_type = profile_type;
        this.job_action = job_action;
        this.job_output = job_output;
        this.time_start = time_start;
        this.time_stop = time_stop;
        this.token_action = token_action;
        this.object_depend = object_depend;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "FacebookIdModel{" +
                "id=" + id +
                ", target_id='" + target_id + '\'' +
                ", profile_type='" + profile_type + '\'' +
                ", job_action='" + job_action + '\'' +
                ", job_output='" + job_output + '\'' +
                ", time_start='" + time_start + '\'' +
                ", time_stop='" + time_stop + '\'' +
                ", token_action='" + token_action + '\'' +
                ", object_depend='" + object_depend + '\'' +
                ", priority='" + priority + '\'' +
                '}';
    }
}
