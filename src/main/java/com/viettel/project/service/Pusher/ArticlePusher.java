package com.viettel.project.service.Pusher;

import com.viettel.project.schema.ArticleResSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class ArticlePusher {
    private final Logger logger = LoggerFactory.getLogger(ArticlePusher.class);

    public static ArticleResSchema pushLink(List<String> articleLink){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<List<String>> request =
                new HttpEntity<>(articleLink, headers);
        return restTemplate.postForObject(" http://203.13.152.10:8913/api/web/monitor/add/queue",request, ArticleResSchema.class);
    }
}
