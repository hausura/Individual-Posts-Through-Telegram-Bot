package com.io.project.service.Pusher.FacebookPusher.Strategy;

import com.io.project.config.AllConfig;
import com.io.project.config.Define;
import com.io.project.model.FacebookIdModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class FbTargetIdPusher implements FbPushStrategy {
    private static final Logger logger = LoggerFactory.getLogger(FbTargetIdPusher.class);
    private final AllConfig allConfig = new AllConfig();

    @Override
    public String push(String target_id) {
        try {
            FacebookIdModel facebookIdModel = new FacebookIdModel(target_id);
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
        return "Error push failed!";
    }
}
