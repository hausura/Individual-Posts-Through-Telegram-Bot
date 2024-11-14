package com.io.project.Utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class BlackListCheck {
    private final Logger logger = LoggerFactory.getLogger(BlackListCheck.class);
    public boolean check(Long userId) {
        try (BufferedReader br =new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/config/blackList.txt"))))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().equals(userId.toString())) {
                    return true;  // User ID found in blacklist
                }
            }
        } catch (IOException e) {
            logger.error("Black list check error: {}",e);
        }
        return false;  // User ID not found in blacklist
    }
}