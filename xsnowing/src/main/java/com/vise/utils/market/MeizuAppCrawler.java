package com.vise.utils.market;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 修复版：Android平台异步爬取魅族应用商店APP名称
 */
public class MeizuAppCrawler {
    private static final String TAG = "MeizuAppCrawler";
    private static final String MEIZU_APP_URL = "https://app.meizu.com/apps/public/detail?package_name=";

    // 单例OkHttpClient
    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
            .build();

    // 主线程Handler
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    // 回调接口
    public interface OnAppNameCallback {
        void onSuccess(String appName, String iconUrl);

        void onFail(String errorMsg);
    }

    /**
     * 异步爬取指定包名的APP名称（修复版）
     */
    protected static void getAppNameAsync(String packageName, OnAppNameCallback callback) {
        if (packageName == null || packageName.isEmpty()) {
            MAIN_HANDLER.post(() -> callback.onFail("包名不能为空"));
            return;
        }

        String url = MEIZU_APP_URL + packageName;

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 13; Pixel 6 Pro Build/TQ3A.230805.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/120.0.6099.144 Mobile Safari/537.36")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                .get()
                .build();

        OK_HTTP_CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String errorMsg = "网络请求失败：" + e.getMessage();
                // Log.e(TAG, errorMsg);
                MAIN_HANDLER.post(() -> callback.onFail(errorMsg));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) {
                        String errorMsg = "请求失败，状态码：" + response.code();
                        // Log.e(TAG, errorMsg);
                        MAIN_HANDLER.post(() -> callback.onFail(errorMsg));
                        return;
                    }

                    // 读取响应体（指定UTF-8编码）
                    String html = response.body().string();
                    // 解析APP名称（修复核心）
                    String appName = parseAppName(html);
                    String appIcon = parseAppIcon(html);

                    if (!TextUtils.isEmpty(appName)) {
                        MAIN_HANDLER.post(() -> callback.onSuccess(appName, appIcon));
                    } else {
                        String errorMsg = "未找到该包名对应的APP名称";
                        // Log.w(TAG, errorMsg + "：" + packageName);
                        MAIN_HANDLER.post(() -> callback.onFail(errorMsg));
                    }
                } catch (Exception e) {
                    String errorMsg = "解析失败：" + e.getMessage();
                    // Log.e(TAG, errorMsg);
                    MAIN_HANDLER.post(() -> callback.onFail(errorMsg));
                } finally {
                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            }
        });
    }

    /**
     * 修复版解析方法：
     * 1. 修正元素选择器
     * 2. 处理Unicode转义编码
     * 3. 增加多位置兜底解析
     */
    private static String parseAppName(String html) {
        try {
            Document doc = Jsoup.parse(html);

            // 方式1：从h3标签解析（主要位置）
            Element appNameElement = doc.selectFirst("div.detail_top > h3");
            if (appNameElement != null) {
                String appName = appNameElement.text().trim();
                // 解码Unicode转义字符
                appName = decodeUnicode(appName);
                // 清理多余后缀（如"-出行更简单，AI更懂你"）
                appName = cleanAppNameSuffix(appName);
                if (!appName.isEmpty()) {
                    return appName;
                }
            }

            // 方式2：从隐藏input的data-name属性解析（备用）
            Element dataNameElement = doc.selectFirst("input#count");
            if (dataNameElement != null) {
                String dataName = dataNameElement.attr("data-name");
                if (dataName != null && !dataName.isEmpty()) {
                    String appName = decodeUnicode(dataName);
                    appName = cleanAppNameSuffix(appName);
                    return appName;
                }
            }

            // 方式3：从title标签解析（最后兜底）
            String title = doc.title();
            if (title != null && !title.isEmpty() && !title.contains("魅族应用商店")) {
                String appName = decodeUnicode(title);
                appName = cleanAppNameSuffix(appName);
                return appName;
            }

            return null;
        } catch (Exception e) {
            // Log.e(TAG, "解析" + packageName + "失败：" + e.getMessage());
            return null;
        }
    }

    /**
     * 解码Unicode转义字符（如&#x767e;&#x5ea6;&#x5730;&#x56fe; → 百度地图）
     */
    private static String decodeUnicode(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }

        // 匹配&#x开头的Unicode转义
        Pattern pattern = Pattern.compile("&#x([0-9a-fA-F]+);");
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String hex = matcher.group(1);
            int code = Integer.parseInt(hex, 16);
            matcher.appendReplacement(sb, String.valueOf((char) code));
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    /**
     * 清理APP名称后的多余后缀（如"-出行更简单，AI更懂你"）
     */
    private static String cleanAppNameSuffix(String appName) {
        if (appName == null || appName.isEmpty()) {
            return "";
        }
        // 按常见分隔符分割，只保留主名称
        String[] parts = appName.split("[-—：:]");
        return parts[0].trim();
    }

    private static String parseAppIcon(String html) {
        // 使用 Jsoup 解析 HTML
        Document document = Jsoup.parse(html);
        Elements elements = document.select("img");
        for (int i = 0; i < elements.size(); i++) {
            Element e = elements.get(i);
            try {
                String clazz = e.className();
                if (!TextUtils.isEmpty(clazz) && "app_img".equals(clazz)) {
                    String s = e.attr("src");
                    if (s.contains("mzres")) {
                        continue;
                    }
                    return s;
                }
            } catch (Throwable throwable) {

            }
        }
        return "";
    }
}