package com.mycode.base.retrofitextension.utility;

import android.content.Context;
import android.util.Log;

import com.mycode.base.retrofitextension.REApis;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import androidx.annotation.WorkerThread;

/**
 * Created by kyunghoon on 2019-03-19
 *
 * {@link REApis} 모듈을 쓰지않고 {@link HttpURLConnection}을 사용하여 단순하게 Request 를 날릴 수 있는 Util 성 클래스
 *
 */
public class SimpleHttpRequest {
    private static final String TAG = "SimpleHttpRequest";

    private static final int CONNECTION_TIMEOUT = 30000;
    private static final int READ_TIMEOUT = 30000;

    @WorkerThread
    public static void sendGet(String url, Map<String, String> header) {
        Log.d(TAG, "###v simple http call : " + url);

        HttpURLConnection conn = null;
        try {
            conn = getGetConnection(url, header);
            InputStream in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 8];
            int length = 0;
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }

            Log.d(TAG, "###v simple http call result : " + new String(out.toByteArray(), "UTF-8"));
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static HttpURLConnection getGetConnection(String urlString, Map<String, String> header) throws Throwable {
        URL url = new URL(urlString);
        HttpURLConnection conn = getHttpURLConnection(url, header);
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(CONNECTION_TIMEOUT);
        conn.setReadTimeout(READ_TIMEOUT);
        return conn;
    }

    @NotNull
    private static HttpURLConnection getHttpURLConnection(URL url, Map<String, String> header) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (header != null) {
            Set<String> keys = header.keySet();
            for (String key : keys) {
                String value = header.get(key);
                conn.setRequestProperty(key, value);
            }
        }
        return conn;
    }

    private static HttpURLConnection getPostConnection(String urlString, Map<String, String> header) throws Throwable {
        URL url = new URL(urlString);

        HttpURLConnection conn = getHttpURLConnection(url, header);
        conn.setRequestProperty("Accept-Encoding", ""); // android issue
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setConnectTimeout(CONNECTION_TIMEOUT);
        conn.setReadTimeout(READ_TIMEOUT);
        return conn;
    }


    /**
     * memo. 서비스 도메인 정보가 필요해서 클래스 따로 분리합니다.
     */
    public static class ForService {
        private static final String KEY_COOKIE = "Cookie";
        private static final String KEY_USER_AGENT = "User-Agent";

        @WorkerThread
        public static void sendGet(Context context, String url) {
            Map<String, String> header = new HashMap<>();
//            header.put(KEY_COOKIE, LoginManager.getCookie());
//            header.put(KEY_USER_AGENT, UserAgentHelper.api(context));
            SimpleHttpRequest.sendGet(url, header);
        }

        public static void sendGetAsync(Context context, String url) {
            new Thread(() -> {
                sendGet(context, url);
            }).start();
        }
    }


}
