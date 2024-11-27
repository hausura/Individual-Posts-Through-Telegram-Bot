package com.io.project.bot.handlers.CommandHandler.logic.post;

import com.io.project.bot.handlers.CommandHandler.extractor.Extractor;
import com.io.project.bot.rule.RuleRequest;
import com.io.project.config.AllConfig;
import com.io.project.config.Define;
import org.jetbrains.annotations.Nullable;
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

        // Check num push
        if (ruleRequest.checkNumPush(userId) == 0) {
            response.setText("Your push time over limit.");
            return;
        }

        CheckPatternHandler handler = getCheckPatternHandler(ruleRequest, simpleCommand, action);

        if (handler != null) {
            if(handler.getAction().isEmpty()) response.setText("Wrong action.");

            else if(handler.processUrls(userId, urlList, response)){
                // TO DO: add API handle
                handler.pushData(urlList,response);
                ruleRequest.updateNumPush(userId);
            }
        }
    }

    private static @Nullable CheckPatternHandler getCheckPatternHandler(RuleRequest ruleRequest, String simpleCommand, String action) {
        AllConfig allConfig = new AllConfig();
        allConfig.loadConfig();
        CheckPatternHandler handler;
        if (simpleCommand.equals(Define.FB_ID_COMMAND)  && action.equals(Define.FB_ID_ACTION)) {
            handler = new CheckPatternHandler(allConfig.getFB_ID_REGEX(), "facebook", ruleRequest, action);
        } else if (simpleCommand.equals(Define.FB_URL_COMMAND) && action.equals(Define.FB_URL_ACTION)) {
            handler = new CheckPatternHandler(allConfig.getFB_URL_REGEX(), "facebook", ruleRequest, action);
        } else if (simpleCommand.equals(Define.YT_COMMAND)) {
            handler = new CheckPatternHandler(allConfig.getYOUTUBE_REGEX(), "youtube", ruleRequest, action);
        } else if (simpleCommand.equals(Define.TIKTOK_COMMAND)&& action.equals(Define.TIKTOK_URL_COMMENT_ACTION)) {
            handler = new CheckPatternHandler(allConfig.getTIKTOK_REGEX(), "tiktok", ruleRequest, action);
        } else if (simpleCommand.equals(Define.TIKTOK_COMMAND)&& action.equals(Define.TIKTOK_URL_INFO_ACTION)) {
            handler = new CheckPatternHandler(allConfig.getTIKTOK_REGEX(), "tiktok", ruleRequest, action);
        } else if (simpleCommand.equals(Define.TIKTOK_COMMAND)&& action.equals(Define.TIKTOK_KEYWORD_ACTION)) {
            handler = new CheckPatternHandler(".*", "tiktok", ruleRequest, action);
        } else if (simpleCommand.equals(Define.AT_COMMAND)) {
            handler = new CheckPatternHandler(allConfig.getARTICLE_REGEX(), "article", ruleRequest, action);
        } else {
            handler = new CheckPatternHandler("","",ruleRequest,"");
        }
        return handler;
    }
}
