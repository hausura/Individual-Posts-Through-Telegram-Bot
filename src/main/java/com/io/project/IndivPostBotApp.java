package com.io.project;

import com.io.project.bot.SimpleBot;
import com.io.project.config.AllConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class IndivPostBotApp {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
    private static final Logger logger = LoggerFactory.getLogger(IndivPostBotApp.class);

    public static void main(String[] args) {
        try {
            logger.info("Start chatbot");
            // Create an instance of TelegramBotsApi
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            // Config reload Register your bot
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    logger.info("Start register chatbot");
                    AllConfig allConfig =new AllConfig();

                    logger.info("Load config");
                    allConfig.loadConfig();

                    botsApi.registerBot(new SimpleBot(allConfig));
                    logger.info("Chatbot register success!");
                } catch (TelegramApiException e) {
                    logger.error("Error chatbot register: {}",e);
                }
            }, 0, 24, TimeUnit.HOURS);

            scheduler.schedule(() -> System.exit(-1), 90, TimeUnit.HOURS);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}