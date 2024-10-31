package com.viettel.project.bot.handlers;

import com.viettel.project.service.GeminiApiService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MessageHandler {
    private final GeminiApiService geminiApiClient = new GeminiApiService();

    public SendMessage handleMessage(Update update){
        String chatId = update.getMessage().getChatId().toString();
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        String userMessage = update.getMessage().getText().toLowerCase();
        List<String> welcome= Arrays.asList("chào","hello","hi","hii");

        if (welcome.contains(userMessage)) {
            response.setText("Hello!");
            return response;
        }

        try {
            String geminiResponse = geminiApiClient.getGeminiResponse(userMessage);
            response.setText(geminiResponse);
        } catch (IOException e) {
            response.setText("Sorry, an error occurred.");
        }
        return response;    }
}
