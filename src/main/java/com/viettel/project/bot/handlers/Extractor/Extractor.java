package com.viettel.project.bot.handlers.Extractor;

import com.viettel.project.bot.handlers.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class Extractor {
    public static final Logger logger = LoggerFactory.getLogger(Extractor.class);

    public static String extractPassword(String rawPassword){
        int rawPasswordLength= rawPassword.length();
        String lastSymbol=rawPassword.substring(rawPasswordLength-1,rawPasswordLength);
        if (Arrays.asList(rawPassword.split("<")).size() <2| !lastSymbol.equals(">")) {
            return "";
        }
        return Arrays.asList(rawPassword.split("<")).get(1).replace(">","");
    }

    public static List<String> extractLink(String fullCommand, String simpleCommand){
        String urlListString=fullCommand.replace(simpleCommand,"").strip();
        List<String> urlList = Arrays.asList(urlListString.split("\\s+"));
        logger.info("url: {}",urlList);
        return urlList;
    }
}
