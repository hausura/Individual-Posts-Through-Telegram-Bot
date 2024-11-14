package com.io.project.service;

import com.io.project.config.AllConfig;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class GeminiApiService {
    private String API_KEY ;
    private final String GEMINI_URL;

    public GeminiApiService(AllConfig allConfig){
        API_KEY = allConfig.geminiApiKey();
        GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;
    }

    public String getGeminiResponse(String userPrompt) throws IOException {
        OkHttpClient client = new OkHttpClient();

        String jsonPayload = "{\n" +
                "  \"contents\": [{\n" +
                "    \"parts\": [{\n" +
                "      \"text\": \"" + userPrompt + "\"\n" +
                "    }]\n" +
                "  }]\n" +
                "}";

        RequestBody body = RequestBody.create(jsonPayload, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(GEMINI_URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        // Execute the request
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return parseMessageText(response.body().string());
            } else {
                return "Bot api request failed: " + response.message();
            }
        }
    }


    private String parseMessageText(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);

        // Navigate through the JSON structure
        JSONArray candidates = jsonObject.getJSONArray("candidates");
        if (candidates.length() > 0) {
            JSONObject content = candidates.getJSONObject(0).getJSONObject("content");
            JSONArray parts = content.getJSONArray("parts");
            if (parts.length() > 0) {
                return parts.getJSONObject(0).getString("text");
            }
        }
        return "No message text found.";
    }
}
