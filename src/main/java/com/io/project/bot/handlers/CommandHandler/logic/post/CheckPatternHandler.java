package com.io.project.bot.handlers.CommandHandler.logic.post;

import com.io.project.bot.rule.RuleRequest;
import com.io.project.model.FacebookIdModel;
import com.io.project.service.Pusher.ArticlePusher;
import com.io.project.service.Pusher.FacebookPusher;
import com.io.project.service.Pusher.YoutubePusher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CheckPatternHandler {
    private static final Logger logger = LoggerFactory.getLogger(CheckPatternHandler.class);

    private final String patternStr;
    private final String platform;
    private final RuleRequest ruleRequest;
    private final String action;

    public CheckPatternHandler(String patternStr, String platform, RuleRequest ruleRequest,String action) {
        this.patternStr = patternStr;
        this.platform = platform;
        this.ruleRequest = ruleRequest;
        this.action = action;
    }

    public boolean processUrls(Long userId, List<String> urlList, SendMessage response) {
        StringBuilder doneIndexUrl = new StringBuilder();
        List<String> errorLinks = new ArrayList<>();
        Pattern pattern = Pattern.compile(patternStr);

        for (String url : urlList) {
            if (pattern.matcher(url).matches() && ruleRequest.checkNumPush(userId) == 1) {
                doneIndexUrl.append(",").append(urlList.indexOf(url));
                logger.info("Pass url index: {}", doneIndexUrl);
            } else {
                errorLinks.add(url);
                logger.info("Wrong url pattern for: {}", platform);
            }
        }
        logger.info("Verified/matched link index: {}", doneIndexUrl);

        if (errorLinks.isEmpty()) {
            response.setText("All links passed matched pattern");
            logger.info("All links passed matched pattern");
            return true;
        } else {
            response.setText("Missing action /Failed to push " + platform + " for some URLs, Please try again.");
        }
        return false;
    }

    public void pushData(List<String> urlOrId, SendMessage response) {
        switch (platform) {
            case "facebook_target_id" -> {
                FacebookPusher facebookPusher = new FacebookPusher();
                FacebookIdModel facebookIdModel = new FacebookIdModel(urlOrId.get(0));
                String res = facebookPusher.pushTargetIdData(facebookIdModel).toString();
                response.setText(res);
            }
            case "facebook_url" -> {
                FacebookPusher facebookPusher = new FacebookPusher();
                String res = facebookPusher.pushLink(urlOrId.get(0)).toString();
                response.setText(res);
            }
            case "youtube" -> {
                YoutubePusher youtubePusher = new YoutubePusher();
                String res = youtubePusher.pushLink(urlOrId,action).toString();
                response.setText(res);
            }
            case "tiktok" -> {
                // TO DO
            }
            case "article" -> {
                ArticlePusher articlePusher = new ArticlePusher();
                String res = articlePusher.pushLink(urlOrId);
                response.setText(res.toString());
            }
        }
    }
}
