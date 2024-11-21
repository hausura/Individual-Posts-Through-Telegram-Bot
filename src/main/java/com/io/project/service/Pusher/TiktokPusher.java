package com.io.project.service.Pusher;

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

public class TiktokPusher {
    private static final Logger logger = LoggerFactory.getLogger(TiktokPusher.class);
    private final AllConfig allConfig = new AllConfig();
    public String pushLink(List<String> youtubeLinkList,String action) {
        try {
            allConfig.loadConfig();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            RestTemplate restTemplate = new RestTemplate();
            List<TiktokModel> youtubeModelList = new ArrayList<>();

            for (String tiktokLink : youtubeLinkList) {
                TiktokModel tiktokModel = new TiktokModel(tiktokLink,action);
                youtubeModelList.add(tiktokModel);
            }

            // Create an HttpEntity with the list of TiktokModel objects
            logger.info("Start push youtube data to server");
            HttpEntity<List<TiktokModel>> request = new HttpEntity<>(youtubeModelList, headers);

            // Log info
            logger.info(Define.API_URL_LOG,allConfig.getYoutubeApi());
            logger.info(Define.REQUEST_LOG,request);

            return restTemplate.postForObject(allConfig.getYoutubeApi(), request, String.class);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
        }
        return "Error push failed!";
    }
}
