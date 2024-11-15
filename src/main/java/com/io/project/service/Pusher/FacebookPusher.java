package com.io.project.service.Pusher;

import com.io.project.config.AllConfig;
import com.io.project.config.Define;
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
    private final AllConfig allConfig = new AllConfig();

    public String pushTargetIdData(FacebookIdModel facebookIdModel) {
        try {
            allConfig.loadConfig();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            RestTemplate restTemplate = new RestTemplate();

            logger.info("Start push facebook targetId data to server");
            HttpEntity<String> request =
                    new HttpEntity<>(facebookIdModel.toString(), headers);

            // Log info
            logger.info(Define.API_URL_LOG,allConfig.getFacebookApiTarget());
            logger.info(Define.REQUEST_LOG,request);

            return restTemplate.postForObject(allConfig.getFacebookApiTarget(), request, String.class);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
        }
        return "error";
    }

    public String pushLink(List<String> fbLink){
        try {
            allConfig.loadConfig();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            RestTemplate restTemplate = new RestTemplate();

            logger.info("Start push facebook url data to server");
            HttpEntity<List<String>> request =
                    new HttpEntity<>(fbLink, headers);

            // Log info
            logger.info(Define.API_URL_LOG,allConfig.getFacebookApiUrl());
            logger.info(Define.REQUEST_LOG,request);

            return restTemplate.postForObject(allConfig.getFacebookApiUrl(),request, String.class);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
        }
        return "error";
    }
}
