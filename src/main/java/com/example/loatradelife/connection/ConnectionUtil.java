package com.example.loatradelife.connection;

import com.example.loatradelife.config.LostArkOpenApiConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@RequiredArgsConstructor
public class ConnectionUtil {
    private final LostArkOpenApiConfig lostArkOpenApiConfig;

    public HttpURLConnection getApiHttpURLConnection(String url) {
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(lostArkOpenApiConfig.getBaseUrl() + url).openConnection();
            connection.setRequestProperty("authorization", "bearer " + lostArkOpenApiConfig.getKey());
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestMethod("GET");
            connection.connect();

            return connection;
        } catch (IOException e) {
            throw new RuntimeException(e);
            // connection exception
        }
    }

    public HttpURLConnection getHtmlHttpURLConnection(String url) {
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("accept", "text/html");
            connection.setRequestMethod("GET");
            connection.connect();

            return connection;
        } catch (IOException e) {
            throw new RuntimeException(e);
            // connection exception
        }
    }
}
