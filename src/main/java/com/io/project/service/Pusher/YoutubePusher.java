package com.io.project.service.Pusher;

import com.io.project.config.AllConfig;
import com.io.project.model.YoutubeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class YoutubePusher {
    private static final Logger logger = LoggerFactory.getLogger(YoutubePusher.class);
    private final AllConfig allConfig = new AllConfig();
    public String pushLink(List<String> youtubeLinkList,String action) {
        try {
            allConfig.loadConfig();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            RestTemplate restTemplate = new RestTemplate();
            List<YoutubeModel> youtubeModelList = new ArrayList<>();

            for (String youtubeLink : youtubeLinkList) {
                YoutubeModel youtubeModel = new YoutubeModel(youtubeLink,action);
                youtubeModelList.add(youtubeModel);
            }

            // Create an HttpEntity with the list of YoutubeModel objects
            logger.info("Start push youtube data to server");
            HttpEntity<List<YoutubeModel>> request = new HttpEntity<>(youtubeModelList, headers);
            return restTemplate.postForObject(allConfig.getYoutubeApi(), request, String.class);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
        }
        return "error";
    }
}
