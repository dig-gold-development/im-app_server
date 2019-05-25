package cn.wildfirechat.sdk;

import cn.wildfirechat.sdk.model.IMResult;
import com.google.gson.Gson;
import ikidou.reflect.TypeBuilder;
import okhttp3.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.CoreConnectionPNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;


public class HttpUtils {
    private static final Logger LOG = LoggerFactory.getLogger(HttpUtils.class);

    private static String adminUrl;
    private static String adminSecret;

    static void init(String url, String secret) {
        adminUrl = url;
        adminSecret = secret;
    }

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    static private OkHttpClient client = new OkHttpClient();

    static <T> IMResult<T> httpJsonPost(String path, Object object, Class<T> clazz) throws Exception{
        if (isNullOrEmpty(adminUrl) || isNullOrEmpty(path)) {
            LOG.error("Not init IM SDK correctly. Do you forget init it?");
            throw new Exception("SDK url or secret lack!");
        }

        String url = adminUrl + path;
        try {
            int nonce = (int)(Math.random() * 100000 + 3);
            long timestamp = System.currentTimeMillis();
            String str = nonce + "|" + adminSecret + "|" + timestamp;
            String sign = DigestUtils.sha1Hex(str);



            String jsonStr = new Gson().toJson(object);
            LOG.info("http request content: {}", jsonStr);



            RequestBody body = RequestBody.create(JSON, jsonStr);
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Connection", "Keep-Alive")
                    .addHeader("nonce", nonce + "")
                    .addHeader("timestamp", "" + timestamp)
                    .addHeader("sign", sign)
                    .post(body)
                    .build();

            CountDownLatch latch = new CountDownLatch(1);
            final List<String> responseContainer = new ArrayList<String>();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    LOG.info("POST to {} with data {} failure", url, jsonStr);
                    latch.countDown();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body().string();
                    responseContainer.add(responseBody);
                    LOG.info("POST to {} success with response", url, responseBody);
                    latch.countDown();
                }
            });


            latch.await();

            if (responseContainer.size() == 0) {
                return null;
            }

            return fromJsonObject(responseContainer.get(0), clazz);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static <T> IMResult<T> fromJsonObject(String content, Class<T> clazz) {
        Type type = TypeBuilder
                .newInstance(IMResult.class)
                .addTypeParam(clazz)
                .build();
        return new Gson().fromJson(content, type);
    }

    private static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

}
