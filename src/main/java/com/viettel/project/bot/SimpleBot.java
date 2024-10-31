package com.viettel.project.bot;

import com.viettel.project.bot.handlers.CommandHandler;
import com.viettel.project.bot.handlers.MessageHandler;
import com.viettel.project.bot.logic.RuleRequest;
import com.viettel.project.config.AllConfig;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class SimpleBot extends TelegramLongPollingBot {
    private final CommandHandler commandHandler;
    private final MessageHandler messageHandler;
    private AllConfig allConfig = new AllConfig();


    public SimpleBot() {
        allConfig.loadConfig();
        this.commandHandler = new CommandHandler(allConfig);
        this.messageHandler = new MessageHandler();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            try {
                if (messageText.startsWith("/")) {
                    SendMessage res = commandHandler.handleCommand(update);
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
