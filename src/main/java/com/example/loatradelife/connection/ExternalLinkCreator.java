package com.example.loatradelife.connection;

import com.example.loatradelife.config.LostArkOpenApiConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExternalLinkCreator {
    private final LostArkOpenApiConfig lostArkOpenApiConfig;

    public HttpURLConnection getApiHttpURLConnection(String url) {
        HttpURLConnection connection;
        try {
            URL fullUrl = new URL(lostArkOpenApiConfig.getBaseUrl() + url);
            connection = (HttpURLConnection) fullUrl.openConnection();
            connection.setRequestProperty("authorization", "bearer " + lostArkOpenApiConfig.getKey());
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == 403) {
                log.error("check: 403 authorization-bearer");
                throw new RuntimeException();
            }

            return connection;
        } catch (IOException e) {
            log.error("check: parameter url, api server alive", e);
            throw new RuntimeException();
        }
    }

    public Document getJsopDocument(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            log.error("check: parameter url, api server alive");
            throw new RuntimeException();
        }
    }
}
