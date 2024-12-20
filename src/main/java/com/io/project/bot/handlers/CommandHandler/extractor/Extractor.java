package com.io.project.bot.handlers.CommandHandler.extractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class Extractor {
    public static final Logger logger = LoggerFactory.getLogger(Extractor.class);

    public static String extractPassword(String rawPassword){
        if (Arrays.asList(rawPassword.split(" ")).size() <2) {
            return "";
        }
        return Arrays.asList(rawPassword.split(" ")).get(1);
    }

    public static List<String> extractLink(String fullCommand, String simpleCommand,String action){
        String urlListString=fullCommand.replace(simpleCommand,"").replace(action,"").strip();
        List<String> urlList = Arrays.asList(urlListString.split("\\s+"));
        logger.info("url: {}",urlList);
        return urlList;
    }
}
