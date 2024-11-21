package com.io.project.bot.handlers.CommandHandler.logic.password;

import com.io.project.bot.handlers.CommandHandler.extractor.Extractor;
import com.io.project.config.Define;
import com.io.project.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class HandlePasswordCommand {
    private static final Logger logger = LoggerFactory.getLogger(HandlePasswordCommand.class);

    public static void handle(String command, Long userId, SendMessage response, AuthService authService){
        String password = Extractor.extractPassword(command).strip();

        if (!password.isEmpty()) {
            boolean isAuthenticated = authService.authenticate(userId, password);
            logger.info("user password:{}", password);

            if (isAuthenticated) {
                authService.updateLoginStt(userId,true);
                response.setText("""
                        Input password success!\s
                        Please input your url/id by command below: \n
                        """
                        +
                        Define.FB_URL_COMMAND + " " + Define.FB_URL_ACTION + " " +
                        "<insert_your_link_here> \n" +
                        Define.FB_ID_COMMAND + " " + Define.FB_ID_ACTION + " " +
                        "<insert_your_link_here> \n" +
                        Define.YT_COMMAND + " <action> " +
                        "<insert_your_link_here> \n" +
                        Define.TIKTOK_COMMAND + " " + Define.TIKTOK_URL_COMMENT_ACTION + " " +
                        "<insert_your_link_here> \n" +
                        Define.TIKTOK_COMMAND + " " + Define.TIKTOK_URL_INFO_ACTION + " " +
                        "<insert_your_link_here> \n" +
                        Define.TIKTOK_COMMAND + " " + Define.TIKTOK_KEYWORD_ACTION + " " +
                        "<insert_your_keyword_here> \n" +
                        Define.AT_COMMAND + " <action> " +
                        "<insert_your_link_here> \n"
                );
            } else {
                response.setText("Wrong password, please try again.");
            }
        }
    }
}
