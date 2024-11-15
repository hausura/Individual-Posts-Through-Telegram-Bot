package com.io.project.bot.handlers.CommandHandler.logic.post;

import com.io.project.bot.handlers.CommandHandler.extractor.Extractor;
import com.io.project.bot.rule.RuleRequest;
import com.io.project.config.AllConfig;
import com.io.project.config.Define;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Arrays;
import java.util.List;

public class PostHandler {
    public static void handle(String command, Long userId, SendMessage response, RuleRequest ruleRequest) {
        String simpleCommand = Arrays.asList(command.split(" ")).get(0);
        String action = Arrays.asList(command.split(" ")).get(1);
        List<String> urlList = Extractor.extractLink(command, simpleCommand,action);

        if (urlList.isEmpty()) {
            response.setText("Please provide url.");
            return;
        }

        if (ruleRequest.checkNumPush(userId) == 0) {
            response.setText("Your push time over limit.");
            return;
        }
        AllConfig allConfig = new AllConfig();
        allConfig.loadConfig();
        CheckPatternHandler handler = switch (simpleCommand) {
            case Define.FB_ID_COMMAND -> new CheckPatternHandler(allConfig.getFB_ID_REGEX(), "facebook_target_id", ruleRequest,action);
            case Define.FB_URL_COMMAND -> new CheckPatternHandler(allConfig.getFB_URL_REGEX()  , "facebook_url", ruleRequest,action);
            case Define.YT_COMMAND -> new CheckPatternHandler(allConfig.getYOUTUBE_REGEX(), "youtube", ruleRequest,action);
            case Define.TIKTOK_COMMAND -> new CheckPatternHandler(allConfig.getTIKTOK_REGEX(), "tiktok", ruleRequest,action);
            case Define.AT_COMMAND -> new CheckPatternHandler(allConfig.getARTICLE_REGEX(), "article", ruleRequest,action);
            default -> null;
        };

        if (handler != null) {
            if(handler.processUrls(userId, urlList, response)){
                // TO DO: add API handle
                handler.pushData(urlList,response);
            }
        }
    }
}
