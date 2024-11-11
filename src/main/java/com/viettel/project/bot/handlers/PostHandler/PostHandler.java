package com.viettel.project.bot.handlers.PostHandler;

import com.viettel.project.bot.logic.RuleRequest;
import com.viettel.project.model.FbPushJobModel;
import com.viettel.project.schema.ArticleResSchema;
import com.viettel.project.service.Pusher.ArticlePusher;
import com.viettel.project.service.Pusher.FacebookPusher;
import com.viettel.project.service.Pusher.YoutubePusher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PostHandler {
    private static final Logger logger = LoggerFactory.getLogger(PostHandler.class);

    private final Pattern pattern;
    private final String platform;
    private final RuleRequest ruleRequest;

    public PostHandler(Pattern pattern, String platform, RuleRequest ruleRequest) {
        this.pattern = pattern;
        this.platform = platform;
        this.ruleRequest = ruleRequest;
    }

    public void processUrls(Long userId, List<String> urlList, SendMessage response) {
        StringBuilder doneIndexUrl = new StringBuilder();
        List<String> errorLinks = new ArrayList<>();

        for (String url : urlList) {
            if (pattern.matcher(url).matches() && ruleRequest.checkNumPush(userId) == 1) {
                doneIndexUrl.append(",").append(urlList.indexOf(url));
            } else {
                errorLinks.add(url);
                logger.info("Wrong url pattern for {}", platform);
            }
        }

        if (errorLinks.isEmpty()) {
            response.setText("All links passed");
        } else {
            response.setText("Failed to push " + platform + " for some URLs.");
            urlList.removeAll(errorLinks);
        }
    }

    public void pushData(List<String> urlOrId, SendMessage response) {
        switch (platform) {
            case "facebook_target_id" -> {
                FacebookPusher facebookPusher = new FacebookPusher();
                FbPushJobModel fbPushJobModel = new FbPushJobModel(urlOrId.get(0));
                String res = facebookPusher.pushTargetIdData(fbPushJobModel);
                if (res.equals("Add message success")) {
                    response.setText("Push success");
                } else {
                    response.setText("Push fail");
                    logger.error("Error post facebook_target_id api: {}",res);
                }
            }
            case "facebook_url" -> {
                FacebookPusher facebookPusher = new FacebookPusher();
                String res = facebookPusher.pushLink(urlOrId);
                if (res.equals("success")) {
                    response.setText("Push success");
                } else {
                    response.setText("Push fail");
                    logger.error("Error post facebook_url api: {}",res);
                }
            }
            case "youtube" -> {
                YoutubePusher youtubePusher = new YoutubePusher();
                String res = youtubePusher.pushLink(urlOrId);
                if (res.equals("Add message success")) {
                    response.setText("Push success");
                } else {
                    response.setText("Push fail");
                    logger.error("Error post youtube api: {}",res);
                }
            }
            case "tiktok" -> {
                // TO DO
            }
            case "article" -> {
                ArticlePusher articlePusher = new ArticlePusher();
                ArticleResSchema res = articlePusher.pushLink(urlOrId);
                if (res.getMessage().equals("Success")) {
                    response.setText("Push success");
                } else {
                    response.setText("Push fail");
                    logger.error("Error post article api: {}",res);
                }            }
        }
    }
}
