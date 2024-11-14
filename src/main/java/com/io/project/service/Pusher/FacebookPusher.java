package com.io.project.service.Pusher;

import com.io.project.model.FacebookIdModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class FacebookPusher {
    private static final Logger logger = LoggerFactory.getLogger(FacebookPusher.class);

    public String pushTargetIdData(FacebookIdModel facebookIdModel) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();

        logger.info("Start push facebook targetId data to server");
        HttpEntity<String> request =
                new HttpEntity<>(facebookIdModel.toString(), headers);
        return restTemplate.postForObject("http://10.30.154.242:8003/push_job", request, String.class);
    }

    public String pushLink(List<String> fbLink){
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            RestTemplate restTemplate = new RestTemplate();

            logger.info("Start push facebook url data to server");
            HttpEntity<List<String>> request =
                    new HttpEntity<>(fbLink, headers);
            return restTemplate.postForObject("http://10.30.154.242:8003/push_job",request, String.class);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
        }
        return "error";
    }
}
