package com.io.project.service.Pusher.TiktokPusher.Strategy;

import com.io.project.config.AllConfig;
import com.io.project.config.Define;
import com.io.project.model.TiktokModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class TiktokInfoPusher implements TiktokPushStrategy{
    private static final Logger logger = LoggerFactory.getLogger(TiktokInfoPusher.class);
    private final AllConfig allConfig = new AllConfig();
    @Override
    public String push(String tiktokLink, String action) {
        try {
            allConfig.loadConfig();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            RestTemplate restTemplate = new RestTemplate();

            // Create List TikTok model
            List<TiktokModel> tiktokModelList = new ArrayList<>();
            TiktokModel tiktokModel = new TiktokModel(tiktokLink,action);

            tiktokModelList.add(tiktokModel);

            // Create an HttpEntity with the list of TiktokModel objects
            logger.info("Start push tiktok data to server");
            HttpEntity<List<TiktokModel>> request = new HttpEntity<>(tiktokModelList, headers);

            // Log info
            logger.info(Define.API_URL_LOG,allConfig.getTiktokApiUrl());
            logger.info(Define.REQUEST_LOG,request);

            return restTemplate.postForObject(allConfig.getTiktokApiUrl(), request, String.class);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
        }
        return "Error push failed!";
    }
}
