package com.viettel.project.bot.handlers;

import com.viettel.project.FileProducer.BlackListWriter;
import com.viettel.project.bot.handlers.Extractor.Extractor;
import com.viettel.project.bot.handlers.PostHandler.PostHandler;
import com.viettel.project.bot.logic.RuleRequest;
import com.viettel.project.config.AllConfig;
import com.viettel.project.service.AuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;
import java.util.regex.Pattern;

public class CommandHandler{
    private final Queue<Long> blackList = new ArrayDeque<>();
    private final AuthService authService;
    private final RuleRequest ruleRequest;
    private final Pattern facebookPattern = Pattern.compile("^https://www\\.facebook\\.com/\\w+/posts/[\\w-]+\\??.*$");
    private final Pattern youtubePattern = Pattern.compile("^https://www\\.youtube\\.com/watch\\?v=[\\w-]+(&.*)?$");
    private final Pattern tiktokPattern = Pattern.compile("^https://www\\.tiktok\\.com/@[\\w.]+/video/\\d+$");
    private final Pattern articlePattern = Pattern.compile("^https://.*$");
    private final Pattern fbTargetIdPattern = Pattern.compile("([A-Za-z0-9_]+)_([A-Za-z0-9_]+)");
    private static final Logger logger = LoggerFactory.getLogger(CommandHandler.class);

    public CommandHandler(AllConfig allConfig) {
        this.authService = new AuthService(allConfig);
        this.ruleRequest = new RuleRequest(allConfig);
    }


    private void handlePasswordCommand(String command,Long userId, SendMessage response){
        String password = Extractor.extractPassword(command).strip();
        if (!authService.isWhitelisted(userId)){
            response.setText("Your account has not been verified");
        }
        else if (!password.isEmpty()) {
            boolean isAuthenticated = authService.authenticate(userId, password);
            logger.info("user password:{}", password);

            if (isAuthenticated) {
                authService.updateLoginStt(userId,true);
                response.setText("""
                        Input password success!\s
                        Please input your token by command below\s
                        /add_facebook_url \
                        insert_your_link_here\s
                        add_facebook_target_id \
                        insert_your_link_here\s
                        /add_youtube \
                        insert_your_link_here\s
                        /add_tiktok \
                        insert_your_link_here\s
                        /add_article \
                        insert_your_link_here\s
                        """);
            } else {
                response.setText("Wrong password, please try again.");
            }
        } else {
            response.setText("Please provide the password following the command: \n " +
                    "/password <your_password>.");
        }
    }

    private void handleAddPostsCommand(String command, Long userId, SendMessage response) {
        String simpleCommand = Arrays.asList(command.split(" ")).get(0);
        List<String> urlList = Extractor.extractLink(command, simpleCommand);

        if (urlList.isEmpty()) {
            response.setText("Please provide url.");
            return;
        }

        if (ruleRequest.checkNumPush(userId) == 0) {
            response.setText("Your push time over limit.");
            return;
        }

        PostHandler handler = null;
        switch (simpleCommand) {
            case "/add_facebook_target_id":
                handler = new PostHandler(facebookPattern, "facebook_target_id", ruleRequest);
                break;
            case "/add_facebook_url":
                handler = new PostHandler(fbTargetIdPattern, "facebook_url", ruleRequest);
                break;
            case "/add_youtube":
                handler = new PostHandler(youtubePattern, "youtube", ruleRequest);
                break;
            case "/add_tiktok":
                handler = new PostHandler(tiktokPattern, "tiktok", ruleRequest);
                break;
            case "/add_article":
                handler = new PostHandler(articlePattern, "article", ruleRequest);
                break;
        }

        if (handler != null) {
            handler.processUrls(userId, urlList, response);
            // TO DO: add API handle
            handler.pushData(urlList,response);
        }
    }

    // Start from here
    public SendMessage handleCommand(Update update) {
        Long userId = update.getMessage().getFrom().getId();
        String chatId = update.getMessage().getChatId().toString();

        String command = update.getMessage().getText();
        Pattern passwordPattern = Pattern.compile("^/password\\s*.*$");

        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        if(blackList.contains(userId)) return response;

        //Start and instruction command
        if (command.equals("/start")) {
            //Get user id
            logger.info("UserID: {}", userId);
            response.setText("Bot is running!");
            return response;
        }

        else if(command.equals("/logout")) {
            //Get user id
            logger.info("UserID: {}", userId);
            response.setText("Log out success!");
            authService.updateLoginStt(userId,false);
            return response;
        }

        else if (command.equals("/help")) {
            response.setText("""
                    Individual Posts bot is responsible for importing Posts into \
                    the crawler system from external network, commands includes:\
                     /start           check bot status\
                     /password        login to command\
                     /help        commands instruction\
                     /add_facebook  add facebook posts\
                     /add_youtube    add youtube posts\
                     /add_tiktok      add tiktok posts\
                     /add_article     add article posts\
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
            handlePasswordCommand(command,userId,response);
        }
        //Case logged in but wrong pass, syntax, ...
        //Not logged in success yet
        else if (!authService.getLoginStatus(userId)) {
            if(command.contains("/add_facebook") | command.contains("/add_youtube") | command.contains("/add_tiktok") | command.contains("/add_article")){
                response.setText("Please provide your password by command /password <insert_your_password_here>");
            }
            else{response.setText("Wrong url syntax.");}
        }
        // 0. Logged in success
        else if (authService.getLoginStatus(userId)) {
            if (command.startsWith("/add")) {
                handleAddPostsCommand(command,userId,response);
            }
            else{response.setText("Wrong url syntax.");}
        }
        return response;
    }
}