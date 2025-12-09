package com.vise.utils.market;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TencentAppCrawler {

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build();

    // 回调接口
    public interface OnAppNameCallback {
        void onSuccess(String appName, String iconUrl);

        void onFail(String errorMsg);
    }

    // 主线程Handler
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    protected static void fetchAppNameFromPackage(String packageName, OnAppNameCallback callback) {
        String url = "https://sj.qq.com/appdetail/" + packageName;

        new Thread(() -> {
            try {
                Request request = new Request.Builder().url(url).build();
                Response response = client.newCall(request).execute();

                if (!response.isSuccessful() || response.body() == null) {
                    throw new Exception("HTTP request failed: " + response.code());
                }

                String html = response.body().string();
                String appName = extractAppName(html);
                String appIcon = parseAppIcon(html);

                if (!TextUtils.isEmpty(appName)) {
                    MAIN_HANDLER.post(() -> callback.onSuccess(appName.trim(), appIcon));
                } else {
                    MAIN_HANDLER.post(() -> callback.onFail("no found."));
                }

            } catch (Exception e) {
                MAIN_HANDLER.post(() -> callback.onFail(e.getMessage()));
            }
        }).start();
    }

    private static String extractAppName(String html) {
        // 使用 Jsoup 解析 HTML
        Document document = Jsoup.parse(html);

        Elements elements = document.select("h1");
        for (int i = 0; i < elements.size(); i++) {
            Element e = elements.get(i);
            if (null == e || TextUtils.isEmpty(e.text())) continue;
            String s = e.text();
            if (s.contains("的相关推荐")) {
                String[] arrs = s.split("的相关推荐");
                if (null != arrs && arrs.length > 0) {
                    return arrs[0].replace("“", "").replace("”", "");
                }
            }
        }

        // 根据 HTML 结构提取应用名称
        // 假设应用名称在 <h1> 标签内，具体根据实际 HTML 结构调整选择器
        Element appNameElement = document.selectFirst("h1");
        if (appNameElement != null) {
            return appNameElement.text();
        }
        return "";
    }


    private static String parseAppIcon(String html) {
        // 使用 Jsoup 解析 HTML
        Document document = Jsoup.parse(html);
        Elements elements = document.select("img");
        for (int i = 0; i < elements.size(); i++) {
            Element e = elements.get(i);
            try {
                String clazz = e.className();
                // jsx-1229924338 GameIcon_icon__vL1er
                if (!TextUtils.isEmpty(clazz) && clazz.contains("GameIcon")) {
                    return e.attr("src");
                }
                String tag = e.attr("loading");
                if (!TextUtils.isEmpty(tag) && "eager".equals(tag)) {
                    return e.attr("src");
                }
            } catch (Throwable throwable) {

            }
        }
        return "";
    }
}