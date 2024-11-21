package com.io.project.bot.handlers.CommandHandler.logic.post;

import com.io.project.bot.rule.RuleRequest;
import com.io.project.service.Pusher.ArticlePusher;
import com.io.project.service.Pusher.FacebookPusher.FbPushContext;
import com.io.project.service.Pusher.TiktokPusher.TiktokPushContext;
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
            logger.info("All links passed matched pattern");
            return true;
        } else {
            response.setText("Not match pattern for: " + platform + " with action: "+action);
        }
        return false;
    }

    public void pushData(List<String> urlOrId, SendMessage response) {
        switch (platform) {
            case "facebook" -> {
                logger.info("true platform fb");
                FbPushContext fbPushContext = new FbPushContext(action);
                String res = fbPushContext.push(urlOrId.get(0));
                response.setText(res);
            }
            case "youtube" -> {
                YoutubePusher youtubePusher = new YoutubePusher();
                String res = youtubePusher.pushLink(urlOrId,action);
                response.setText(res);
            }
            case "tiktok" -> {
                TiktokPushContext tiktokPushContext = new TiktokPushContext(action);
                String res = tiktokPushContext.push(urlOrId.get(0),action);
                response.setText(res);
            }
            case "article" -> {
                ArticlePusher articlePusher = new ArticlePusher();
                String res = articlePusher.pushLink(urlOrId);
                response.setText(res);
            }
        }
    }

    public String getAction() {
        return action;
    }
}
