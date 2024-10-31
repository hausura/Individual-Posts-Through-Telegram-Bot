package com.viettel.project.service;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class GeminiApiService {
    private static final String API_KEY = "AIzaSyCUf7U4-VGZSUpv_r7Vey1ENV04vdFcPoM";
    private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;

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

//    public static void main(String[] args) throws IOException {
//        GeminiApiService geminiApiService = new GeminiApiService();
//        System.out.println(parseMessageText(geminiApiService.getGeminiResponse("xin chào")));
//    }
}
