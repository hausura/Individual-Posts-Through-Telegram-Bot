package com.io.project.crawler.downloader;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;

@Slf4j
public class TiktokDownloader {
    public static String download(String url) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        Request request = new Request.Builder()
                .url("https://www.tiktok.com/@muonmukbangtictocc/")
                .method("GET", null)
                .addHeader("Cookie", "ak_bmsc=A7D27416123541848ED3F2A8E5554361~000000000000000000000000000000~YAAQb/rSFwBpLWuTAQAAgkhlaxlxw/ycTUzkOb9JXiP4/aqQbr7Ad4uuKM0dlnjYRx3K+kmoI9S0Xva6svX/J9XYNcuhvj1nYrgXCHnkxhkiqsBPxeeZLPdzF6XYk2TBoGdFMoG+s3hoFpbX3/nmz1PbdJkJwrriuQge4xTvO3MbgK56h8mG50yVwyjqMIyxCC3vPIP/xqNhwUqDAYK+GfNmkuBMCZU9lNBgEL7C18SRHDqxT8afnueoLSKDCepGSboUmv92HPRKFR8mDJ/J5bYnMpK0NZqB6LlwqZDGoZb4pqhOh2l88SsjvVr+uhiGk5JLr4fCBjzKpbp8MQKLu34j6H8bR9LtS5Hm; msToken=sIgen91mS6U22d4SRmVLWLPSCfMtUXPmlqoo8n5m8SH52dWPsqbyF3DJHd9GZ1dq71U7DHYMlNCzSHa5LL-CiN1OmjOnsVAMx9x7; tt_chain_token=SgsjEYoWffM5b4F1gmzS5w==; tt_csrf_token=h7g7FG5b-Arz9lEGTBUkm7TdLG4JmuVa0nG8; ttwid=1%7ChqQDfWYi557tpG-2rE8lHnHIPA206GZih9T8cOvyvgw%7C1732673619%7Ca074a4074313c430b684dbbc23a081781ceffbbe3cd332cc9f6130eca2f2d531")
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            log.error("Download error: {}",e);
        }
        response.close();
        return "";
    }
}
