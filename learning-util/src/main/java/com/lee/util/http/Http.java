package com.lee.util.http;

import com.lee.util.log.Logging;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Http {


    public static String post(String url, Map<String, Object> params) {

        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.add(key, String.valueOf(params.get(key)));
            }
        }
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        return execute(request);
    }

    public static String get(String url) {
        Request request = new Request.Builder().url(url).get().build();
        return execute(request);
    }

    private static String execute(Request request) {
        OkHttpClient okHttpClient = new OkHttpClient();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseStr = response.body().string();
                Logging.info(responseStr);
                return responseStr;
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] arg) {
        Map<String, Object> request = new HashMap<>();
        request.put("name", "李云龙");
        Http.post("https://www.httpbin.org/post", request);
        Http.get("http://www.baidu.com");
    }
}
