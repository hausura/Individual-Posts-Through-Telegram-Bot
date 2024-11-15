package com.io.project.service.Pusher;

import com.io.project.config.AllConfig;
import com.io.project.config.Define;
import com.io.project.schema.ArticleResSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class ArticlePusher {
    private static final Logger logger = LoggerFactory.getLogger(ArticlePusher.class);
    private final AllConfig allConfig = new AllConfig();

    public ArticleResSchema pushLink(List<String> articleLink){
        try {
            allConfig.loadConfig();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            RestTemplate restTemplate = new RestTemplate();

            logger.info("Start push article data to server");
            HttpEntity<List<String>> request =
                    new HttpEntity<>(articleLink, headers);

            // Log info
            logger.info(Define.API_URL_LOG,allConfig.getArticleApi());
            logger.info(Define.REQUEST_LOG,request);

            return restTemplate.postForObject(allConfig.getArticleApi(), request, ArticleResSchema.class);
        }
        catch (Exception e) {
        logger.error("Unexpected error: {}", e.getMessage(), e);
        }
        return new ArticleResSchema("400","Error","");
    }
}
