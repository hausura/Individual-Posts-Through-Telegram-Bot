package com.io.project.bot;

import com.io.project.bot.handlers.CommandHandler.CommandHandler;
import com.io.project.bot.handlers.MessageHandler;
import com.io.project.config.AllConfig;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class SimpleBot extends TelegramLongPollingBot {
    private final CommandHandler commandHandler;
    private final MessageHandler messageHandler;
    private AllConfig allConfig;


    public SimpleBot(AllConfig allConfig) {
        this.allConfig=allConfig;
        this.commandHandler = new CommandHandler(allConfig);
        this.messageHandler = new MessageHandler(allConfig);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            try {
                if (messageText.startsWith("/")) {
                    SendMessage res = commandHandler.handleCommand(update);
                    execute(res);
                } else if (messageText.contains(allConfig.getBotName())) {
                    SendMessage res = messageHandler.handleMessage(update);
                    execute(res);
                } else {
                    execute(messageHandler.handleMessage(update));
                }
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String getBotUsername() {return allConfig.getBotName();}

    @Override
    public String getBotToken(){return allConfig.getBotToken();}
}
