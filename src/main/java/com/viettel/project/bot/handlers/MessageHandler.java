package com.viettel.project.bot.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

public class MessageHandler {
    public SendMessage handleMessage(Update update){
        String chatId = update.getMessage().getChatId().toString();
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        String message;
        List<String> welcome= Arrays.asList("chào","hello","hi","hii");
        if (welcome.contains(update.getMessage().getText().toLowerCase())) {
            message = "Hello!";
            response.setText(message);
            return response;
        }
        return response;
    }
}
