package com.io.project.bot.handlers.CommandHandler;

import com.io.project.FileProducer.BlackListWriter;
import com.io.project.Utilities.BlackListCheck;
import com.io.project.bot.handlers.CommandHandler.logic.post.PostHandler;
import com.io.project.bot.handlers.CommandHandler.logic.password.HandlePasswordCommand;
import com.io.project.bot.rule.RuleRequest;
import com.io.project.config.AllConfig;
import com.io.project.config.Define;
import com.io.project.service.AuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;
import java.util.regex.Pattern;

public class CommandHandler{
    private static final Logger logger = LoggerFactory.getLogger(CommandHandler.class);
    private final Queue<Long> blackList = new ArrayDeque<>();
    private final AuthService authService;
    private final RuleRequest ruleRequest;
    private final AllConfig allConfig;

    public CommandHandler(AllConfig allConfig) {
        this.authService = new AuthService(allConfig);
        this.ruleRequest = new RuleRequest(allConfig);
        this.allConfig = allConfig;
    }

    // Start from here
    public SendMessage handleCommand(Update update) {
        Long userId = update.getMessage().getFrom().getId();
        String chatId = update.getMessage().getChatId().toString();

        String command = update.getMessage().getText();
        Pattern passwordPattern = Pattern.compile("^/password\\s*.*$");

        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        BlackListCheck blackListCheck = new BlackListCheck();
        if(blackList.contains(userId)| blackListCheck.check(userId)) return response;

        //Start and instruction command
        if (command.equals("/start")) {
            //Get user id
            logger.info("UserID: {}", userId);
            response.setText("Bot is running!");
            return response;
        }

        else if(command.equals("/logout")) {
            //Get user id
            logger.info("UserID logout: {}", userId);
            response.setText("Log out success!");
            authService.updateLoginStt(userId,false);
            return response;
        }

        else if (command.equals("/help")) {
            response.setText("""
                    Individual Posts bot is responsible for importing Posts into \
                    the crawler system from external network, commands includes:\
                    \n
                     /start           check bot status\n
                     /password        login to command\n
                     /help        commands instruction\n
                     /add_facebook  add facebook posts\n
                     /add_youtube    add youtube posts\n
                     /add_tiktok      add tiktok posts\n
                     /add_article     add article posts\n
                     /logout          log out\s""");
        }
        else if (ruleRequest.checkNumLogin(userId)==0) {
            response.setText("Num login over limit");
            if(!blackList.contains(userId)){
                blackList.add(userId);
                BlackListWriter blackListWriter = new BlackListWriter(blackList);
                blackListWriter.writeBlackListToFile();
                logger.info("blackList: {}", blackList);
            }
        }
        //0. login check
        else if (passwordPattern.matcher(command).matches()) {
            ruleRequest.updateNumLogin(userId);
            HandlePasswordCommand.handle(command,userId,response,authService);
        }
        //Case logged in but wrong pass, syntax, ...
        //Not logged in success yet
        else if (!authService.getLoginStatus(userId)) {
            if(command.contains(Define.FB_ID_COMMAND) | command.contains(Define.FB_URL_COMMAND) | command.contains(Define.YT_COMMAND) | command.contains(Define.TIKTOK_COMMAND) | command.contains(Define.AT_COMMAND)){
                response.setText("Please provide your password by command /password <insert_your_password_here>");
            }
            else{response.setText("Wrong url syntax.");}
        }
        // 0. Logged in success
        else if (authService.getLoginStatus(userId)) {
            if (command.startsWith("/add")) {
                PostHandler.handle(command,userId,response,ruleRequest);
            }
            else{response.setText("Wrong url syntax.");}
        }
        return response;
    }
}