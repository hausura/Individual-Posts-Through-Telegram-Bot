package com.io.project.service.Pusher.TiktokPusher.Strategy;

import com.fasterxml.jackson.databind.JsonNode;
import com.io.project.config.AllConfig;
import com.io.project.config.Define;
import com.io.project.crawler.downloader.TiktokDownloader;
import com.io.project.crawler.parser.TiktokParser;
import com.io.project.model.TiktokModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TiktokCommentPusher implements TiktokPushStrategy{
    private static final Logger logger = LoggerFactory.getLogger(TiktokCommentPusher.class);
    private final AllConfig allConfig = new AllConfig();
    @Override
    public String push(String tiktokLink, String action) {
        try {
            allConfig.loadConfig();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            RestTemplate restTemplate = new RestTemplate();

            // Create List TikTok model and get user,video id
            String videoId = getVideoId(tiktokLink);
            String userId = getUserId(tiktokLink);
            String combineId = videoId + "_" + userId;
            List<String> listId = Arrays.asList(combineId);
            List<TiktokModel> tiktokModelList = new ArrayList<>();
            TiktokModel tiktokModel = new TiktokModel(tiktokLink,action);

            tiktokModelList.add(tiktokModel);

            // Create an HttpEntity with the list of TiktokModel objects
            logger.info("Start push tiktok data to server");
            HttpEntity<List<TiktokModel>> request = new HttpEntity<>(tiktokModelList, headers);
            HttpEntity<List<String>> requestWallId = new HttpEntity<>(listId, headers);

            // Log info
            logger.info(Define.API_URL_LOG,allConfig.getTiktokApiUrl());
            logger.info(Define.REQUEST_LOG,request);
            logger.info(Define.REQUEST_LOG,requestWallId);
            logger.info("Response from wall id api:{}",restTemplate.postForObject(allConfig.getTiktokApiWall(), requestWallId, String.class));

            return restTemplate.postForObject(allConfig.getTiktokApiUrl(), request, String.class);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
        }
        return "Error push failed!";
    }

    private String getVideoId(String url){
        String videoId = url.split("/video/")[1];
        logger.info("videoId: {}", videoId);
        return videoId;
    }

    private String getUserId(String url){
        String response = TiktokDownloader.download(url);
        if (StringUtils.isNotEmpty(response)) {
            JsonNode jsonNode = TiktokParser.parseString2JsonNode(TiktokParser.parseScriptObj(response));
            //logger.info("{}", jsonNode.has("__DEFAULT_SCOPE__"));
            if (!jsonNode.isEmpty() && jsonNode.has("__DEFAULT_SCOPE__")) {
                String userId = jsonNode.path("__DEFAULT_SCOPE__").path("webapp.user-detail").path("userInfo").path("user").path("id").asText();
                logger.info("userId: {}", userId);
                return userId;
            }
        }
        return "";
    }
}
