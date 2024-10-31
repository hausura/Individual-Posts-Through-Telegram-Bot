package com.viettel.project;

import com.viettel.project.bot.SimpleBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    public static void main(String[] args) {
        try {
            // Create an instance of TelegramBotsApi
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            // Config reload Register your bot
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    botsApi.registerBot(new SimpleBot());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }, 0, 24, TimeUnit.HOURS);

            scheduler.schedule(() -> System.exit(-1), 90, TimeUnit.HOURS);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}