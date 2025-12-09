package com.vise.utils.market;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Android平台异步爬取小米应用商店APP名称的工具类
 */
public class XiaomiAppCrawler {
    // 小米应用商店基础URL（id参数为包名）
    private static final String XIAOMI_APP_URL = "https://r.app.xiaomi.com/details?id=";

    // 单例OkHttpClient（复用连接池，与魅族爬虫共用也可，这里单独定义便于维护）
    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
            .build();

    // 主线程Handler，用于UI线程回调
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    // 回调接口（与魅族爬虫保持一致，便于统一调用）
    protected interface OnAppNameCallback {
        void onSuccess(String appName, String iconUrl);

        void onFail(String errorMsg);
    }

    /**
     * 异步爬取指定包名的小米应用商店APP名称
     *
     * @param packageName APP包名（对应URL中的id参数）
     * @param callback    结果回调（运行在主线程）
     */
    protected static void getAppNameAsync(String packageName, OnAppNameCallback callback) {
        if (packageName == null || packageName.isEmpty()) {
            MAIN_HANDLER.post(() -> callback.onFail("包名不能为空"));
            return;
        }

        // 构建完整请求URL（小米用id参数传递包名）
        String url = XIAOMI_APP_URL + packageName;

        // 构建OkHttp请求（适配小米应用商店的请求头）
        Request request = new Request.Builder()
                .url(url)
                // .header("User-Agent", "Mozilla/5.0 (Linux; Android 13; Pixel 6 Pro Build/TQ3A.230805.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/120.0.6099.144 Mobile Safari/537.36")
                //  .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                //  .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                //  .header("Referer", "https://app.mi.com/") // 小米应用商店需要Referer验证
                .get()
                .build();

        // 异步执行网络请求
        OK_HTTP_CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String errorMsg = "网络请求失败：" + e.getMessage();
                MAIN_HANDLER.post(() -> callback.onFail(errorMsg));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) {
                        String errorMsg = "请求失败，状态码：" + response.code();
                        MAIN_HANDLER.post(() -> callback.onFail(errorMsg));
                        return;
                    }

                    // 读取响应体（小米返回UTF-8编码）
                    String html = response.body().string();
                    // 解析APP名称（适配小米页面结构）
                    String appName = parseAppName(html);
                    String iconUrl = parseAppIcon(html);

                    if (!TextUtils.isEmpty(appName)) {
                        MAIN_HANDLER.post(() -> callback.onSuccess(appName, iconUrl));
                    } else {
                        String errorMsg = "未找到该包名对应的APP名称";
                        MAIN_HANDLER.post(() -> callback.onFail(errorMsg));
                    }
                } catch (Exception e) {
                    String errorMsg = "解析失败：" + e.getMessage();
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
     * 解析小米应用商店HTML，提取APP名称（多位置兜底）
     */
    private static String parseAppName(String html) {
        try {
            Document doc = Jsoup.parse(html);


            // 方式3：title标签兜底（如<title>植物大战僵尸2 - 小米应用商店</title>）
            String title = doc.title();
            if (title != null && !title.isEmpty()) {
                if (title.contains("手机游戏应用商店") || title.contains("软件商店app下载")) {
                    return "";
                }
                // 分割title，去除" - 小米应用商店"后缀
                if (title.contains("-小米应用商店")) {
                    return title.replace("-小米应用商店", "").trim();
                } else if (title.contains(" - 小米应用商店")) {
                    return title.split(" - 小米应用商店")[0].trim();
                } else if (title.contains("|")) {
                    return title.split("\\|")[0].trim();
                }
                return title.trim();
            }
        } catch (Exception e) {
            // Log.e(TAG, "解析" + packageName + "失败：" + e.getMessage());
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
                if (!TextUtils.isEmpty(clazz) && "yellow-flower".equals(clazz)) {
                    return e.attr("src");
                }
                int width = Integer.parseInt(e.attr("width"));
                if (width == 114) {
                    return e.attr("src");
                }
            } catch (Throwable throwable) {

            }
            // Log.e("===>", e.toString());
        }
        return "";
    }
}
