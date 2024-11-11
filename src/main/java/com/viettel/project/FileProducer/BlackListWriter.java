package com.viettel.project.FileProducer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Queue;

public class BlackListWriter {
    private final Queue<Long> blackList;

    public BlackListWriter(Queue<Long> blackList){
        this.blackList=blackList;
    }

    public void writeBlackListToFile() {
        String filePath = "src/main/resources/config/blackList.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,true))) {
            for (Long id : blackList) {
                writer.write(id.toString());
                writer.newLine();
            }
            System.out.println("Black list written to file successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}