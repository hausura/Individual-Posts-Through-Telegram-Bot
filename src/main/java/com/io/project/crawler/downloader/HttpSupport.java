package com.io.project.crawler.downloader;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.net.Proxy;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HttpSupport {
    private static OkHttpClient client;
    private static HttpSupport httpSupport;

    private HttpSupport() {
        try {
            client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .hostnameVerifier((hostname, session) -> true)
                    .cache(null)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static HttpSupport getInstances() {
        if (httpSupport == null) {
            httpSupport = new HttpSupport();
        }
        return httpSupport;
    }

    public Request createRequest(String url, String userAgent) {
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        url = builder.build().toString();

        if (userAgent.isEmpty())
            userAgent = "";
        return (new Request.Builder()).addHeader("user-agent", userAgent).url(url).build();
    }
    public String sendGetRequest(String url, Proxy proxy, String userAgent) {
        Response response = null;
        try {
            Request request = createRequest(url, userAgent);
            if (proxy != null) {
                response = client.newBuilder().proxy(proxy).build().newCall(request).execute();
            } else {
                response = client.newCall(request).execute();
            }

            // Read the response body once and store it in a variable
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string(); // Read and return the body
            } else {
                log.error("Request failed: " + response.message());
                return null; // Handle unsuccessful response
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null; // Return null or handle the error as needed
        } finally {
            if (response != null) {
                // Close the response body if it was not closed before
                response.close();
            }
        }
    }
}