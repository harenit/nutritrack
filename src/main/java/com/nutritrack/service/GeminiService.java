package com.nutritrack.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

@Service
public class GeminiService {

    @Value("${app.gemini.api-key}")
    private String apiKey;

    @Value("${app.gemini.url}")
    private String apiUrl;

    public String getSuggestion(String item) {
        try {
            URL url = new URL(apiUrl + "?key=" + apiKey);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(15000);

            String prompt = "Suggest a healthy alternative for " + item + " in 2 lines. Be concise and practical.";
            String json = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + prompt + "\" }] }] }";

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            if (conn.getResponseCode() != 200) {
                return "Try a healthier homemade option instead!";
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) response.append(line);
            }

            String res = response.toString();
            int index = res.indexOf("\"text\":");
            if (index != -1) {
                int start = res.indexOf("\"", index + 7) + 1;
                int end = res.indexOf("\"", start);
                if (start > 0 && end > start) {
                    return res.substring(start, end)
                              .replace("\\n", " ")
                              .replace("\\\"", "\"")
                              .trim();
                }
            }
            return "Consider a fresh, whole-food alternative.";

        } catch (Exception e) {
            return "AI suggestion unavailable right now.";
        }
    }
}
