package com.example.loatradelife.connection;

import com.example.loatradelife.config.LostArkOpenApiConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.HttpURLConnection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "loa-open-api.base-url=https://developer-lostark.game.onstove.com",
        "loa-open-api.key=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IktYMk40TkRDSTJ5NTA5NWpjTWk5TllqY2lyZyIsImtpZCI6IktYMk40TkRDSTJ5NTA5NWpjTWk5TllqY2lyZyJ9.eyJpc3MiOiJodHRwczovL2x1ZHkuZ2FtZS5vbnN0b3ZlLmNvbSIsImF1ZCI6Imh0dHBzOi8vbHVkeS5nYW1lLm9uc3RvdmUuY29tL3Jlc291cmNlcyIsImNsaWVudF9pZCI6IjEwMDAwMDAwMDAxNDQ2NDAifQ.J5qCw9ovVfEA6Es67Hy7d7RSljJjVRSDau-6WnVoacXqMMdsA4rLQH00YzSCyQKinqgf5K4fbhetXv8Dv9M1mXHkTLgf-4vqkuACCgqnzusuwjJi4qbZqmzOaf_aw8UgZoB8iuRIeYaIGO64uUhIomZ0BIAM8smdaXR08YhPcQ8jfhk6HqToRPFgZc_cGxggtxyaNGb4XsWL9h1UFsj6SWSx5EDlEZAczSqL73XHWOM3R2c3HzBUk-zdHrM9VGpL2jl_RxQyz79zV_lli43WRUuwvXuP1h6w40oGAZLaBd6ZWq4SjfCvuvtWT44BPSFS5VBKa0MpFN7wTvofeEI5fg"
})
class ConnectionUtilTest {
    @Autowired
    LostArkOpenApiConfig lostArkOpenApiConfig;
    @Autowired
    private ConnectionUtil connectionUtil;

    @Test
    public void getApiConnectionTest() throws IOException {
        String url = "/news/notices";

        HttpURLConnection conn = connectionUtil.getApiHttpURLConnection(url);

        assertThat(conn.getResponseCode()).isEqualTo(HttpURLConnection.HTTP_OK);
    }

    @Test
    public void getHttpConnectionTest() throws IOException {
        String url = "https://lostark.game.onstove.com/News/Notice/Views/2573";

        HttpURLConnection conn = connectionUtil.getHtmlHttpURLConnection(url);

        assertThat(conn.getResponseCode()).isEqualTo(HttpURLConnection.HTTP_OK);
    }
}
