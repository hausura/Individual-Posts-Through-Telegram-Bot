package com.viettel.project.bot.handlers;

import com.viettel.project.bot.logic.RuleRequest;
import com.viettel.project.config.AllConfig;
import com.viettel.project.service.AuthService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.regex.Pattern;

public class CommandHandler{
    private final Queue<Long> blackList = new ArrayDeque<>();
    private final AuthService authService;
    private final RuleRequest ruleRequest;
    private final Pattern facebookPattern = Pattern.compile("^https://www\\.facebook\\.com/\\w+/posts/[\\w-]+\\??.*$");
    private final Pattern youtubePattern = Pattern.compile("^https://www\\.youtube\\.com/watch\\?v=[\\w-]+(&.*)?$");
    private final Pattern tiktokPattern = Pattern.compile("^https://www\\.tiktok\\.com/@[\\w.]+/video/\\d+$");

    public CommandHandler(AllConfig allConfig) {
        this.authService = new AuthService(allConfig);
        this.ruleRequest = new RuleRequest(allConfig);
    }

    private static String extractPassword(String rawPassword){
        int rawPasswordLength= rawPassword.length();
        String lastSymbol=rawPassword.substring(rawPasswordLength-1,rawPasswordLength);
        if (Arrays.asList(rawPassword.split("<")).size() <2| !lastSymbol.equals(">")) {
            return "";
        }
        return Arrays.asList(rawPassword.split("<")).get(1).replace(">","");
    }

    private static String extractLink(String fullCommand,String simpleCommand){
        String url;
        url=fullCommand.replace(simpleCommand,"").strip();
        System.out.println("url: "+url);
        return url;
    }

    private void handlePasswordCommand(String command,Long userId, SendMessage response){
        String password = extractPassword(command).strip();
        if (!authService.isWhitelisted(userId)){
            response.setText("Your account has not been verified");
        }
        else if (!password.isEmpty()) {
            boolean isAuthenticated = authService.authenticate(userId, password);
            System.out.println("user password:"+ password);

            if (isAuthenticated) {
                authService.updateLoginStt(userId,true);
                response.setText("Input password success! \n"+
                        "Please input your token by command below \n"+
                        "/add_facebook "+
                        "insert_your_link_here \n"+
                        "/add_youtube "+
                        "insert_your_link_here \n"+
                        "/add_tiktok "+
                        "insert_your_link_here \n");
            } else {
                response.setText("Wrong password, please try again.");
            }
        } else {
            response.setText("Please provide the password following the command: \n " +
                    "/password <your_password>.");
        }
    }

    private void handleAddPostsCommand(String command,Long userId, SendMessage response){
        List<String> addCommand = Arrays.asList("/add_facebook","/add_youtube","/add_tiktok");
        String simpleCommand= Arrays.asList(command.split(" ")).get(0);
        String url = extractLink(command,simpleCommand);

        if (url.isEmpty()) {response.setText("Please provide url."); return;}

        if (ruleRequest.checkNumPush(userId)==0) {
            response.setText("Your push time over limit.");
        }
        else if (simpleCommand.equals(addCommand.get(0))
                && facebookPattern.matcher(url).matches()){
            response.setText("Push facebook url done.");
            ruleRequest.updateNumPush(userId);
        }
        else if (simpleCommand.equals(addCommand.get(1))
                && youtubePattern.matcher(url).matches()
                && ruleRequest.checkNumPush(userId)==1){
            response.setText("Push youtube url done.");
            ruleRequest.updateNumPush(userId);
        }
        else if (simpleCommand.equals(addCommand.get(2))
                && tiktokPattern.matcher(url).matches()
                && ruleRequest.checkNumPush(userId)==1) {
            response.setText("Push tiktok url done.");
            ruleRequest.updateNumPush(userId);
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
            System.out.println("UserID: "+userId);
            response.setText("Bot is running!");
            return response;
        }

        else if(command.equals("/logout")) {
            //Get user id
            System.out.println("UserID: "+userId);
            response.setText("Log out success!");
            authService.updateLoginStt(userId,false);
            return response;
        }

        else if (command.equals("/help")) {
            response.setText("Individual Posts bot is responsible for importing Posts into " +
                    "the crawler system from external network, commands includes:" +
                    "\n /start           check bot status" +
                    "\n /password        login to command" +
                    "\n /help        commands instruction" +
                    "\n /add_facebook  add facebook posts" +
                    "\n /add_youtube    add youtube posts" +
                    "\n /add_tiktok      add tiktok posts" +
                    "\n /logout          log out ");
        }
        else if (ruleRequest.checkNumLogin(userId)==0) {
            response.setText("Num login over limit");
            if(!blackList.contains(userId)){
                blackList.add(userId);
                System.out.println("blackList: "+blackList);
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
            if(command.contains("/add_facebook") | command.contains("/add_youtube") | command.contains("/add_tiktok")){
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
