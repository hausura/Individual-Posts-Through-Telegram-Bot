package com.io.project.crawler.parser;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

@Slf4j
public class TiktokParser {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static JsonNode parseString2JsonNode(String json) {
        if (StringUtils.isEmpty(json))
            return null;

        try {
            return OBJECT_MAPPER.readTree(json);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }


    public static String parseScriptObj(String html) {
        if (StringUtils.isEmpty(html))
            return null;
        try {
            Document document = Jsoup.parse(html);
            Element element = document.getElementById("SIGI_STATE");
            if (element == null) element = document.getElementById("__NEXT_DATA__");
            if (element == null) element = document.getElementById("__UNIVERSAL_DATA_FOR_REHYDRATION__");
            if (element == null) element = document.getElementById("__DEFAULT_SCOPE__");
            if (element != null) {
                return element.html();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info("Not found script object from html!");
        return null;
    }
}
