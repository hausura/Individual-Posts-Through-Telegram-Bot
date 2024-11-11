package com.viettel.project.service.Pusher;

import com.viettel.project.model.YTPushModel;
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
    public String pushLink(List<String> youtubeLinkList) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();
        List<YTPushModel> ytPushModelList = new ArrayList<>();

        for (String youtubeLink : youtubeLinkList) {
            YTPushModel ytPushModel = new YTPushModel(youtubeLink);
            ytPushModelList.add(ytPushModel);
        }

        // Create an HttpEntity with the list of YTPushModel objects
        HttpEntity<List<YTPushModel>> request = new HttpEntity<>(ytPushModelList, headers);
        return restTemplate.postForObject("http://10.30.154.242:8003/push_job", request, String.class);
    }
}
