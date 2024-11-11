package com.viettel.project.service.Pusher;

import com.viettel.project.model.FbPushJobModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

public class FacebookPusher {
    private static final Logger logger = LoggerFactory.getLogger(FacebookPusher.class);

    public String pushTargetIdData(FbPushJobModel fbPushJobModel) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> request =
                new HttpEntity<>(fbPushJobModel.toString(), headers);
        return restTemplate.postForObject("http://10.30.154.242:8003/push_job", request, String.class);
    }

    public String pushLink(List<String> fbLink){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<List<String>> request =
                new HttpEntity<>(fbLink, headers);
        return restTemplate.postForObject("http://10.30.154.242:8003/push_job",request, String.class);
    }
}
