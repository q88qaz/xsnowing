package com.vise.utils.market;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class AppNameFinder {

    private static final int NUM = 3;
    private static final List<Bean> list = Collections.synchronizedList(new ArrayList<>());
    private static final Map<String, Bean> map = Collections.synchronizedMap(new ArrayMap<>());
    // 主线程Handler，用于UI线程回调
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());
    private static String pg = "";
    private static Callback mCallback;
    private static AtomicInteger counter;
    private static ExecutorService executor;

    public static void searchAppName(String pkg, final Callback cb) {
        if (map.containsKey(pkg)) {
            Bean b = map.get(pkg);
            if (null != b) {
                callNameNoDesc(b, cb);
            } else {
                cb.onFail();
            }
            return;
        }
        if (null != mCallback) {
            return;
        }
        mCallback = cb;
        pg = pkg;
        list.clear();
        counter = new AtomicInteger(NUM);
        executor = Executors.newFixedThreadPool(NUM);
        executor.execute(() -> {
            XiaomiAppCrawler.getAppNameAsync(pkg, new XiaomiAppCrawler.OnAppNameCallback() {
                @Override
                public void onSuccess(String appName, String iconUrl) {
                    Bean b = new Bean();
                    if (!TextUtils.isEmpty(appName)) {
                        b.setAppName(appName);
                    }
                    if (!TextUtils.isEmpty(iconUrl)) {
                        b.setAppIcon(iconUrl);
                    }
                    list.add(0, b);
                    workIt();
                }

                @Override
                public void onFail(String errorMsg) {
                    workIt();
                }
            });
        });
        executor.execute(() -> {
            MeizuAppCrawler.getAppNameAsync(pkg, new MeizuAppCrawler.OnAppNameCallback() {
                @Override
                public void onSuccess(String appName, String iconUrl) {
                    Bean b = new Bean();
                    if (!TextUtils.isEmpty(appName)) {
                        b.setAppName(appName);
                    }
                    if (!TextUtils.isEmpty(iconUrl)) {
                        b.setAppIcon(iconUrl);
                    }
                    list.add(b);
                    workIt();
                }

                @Override
                public void onFail(String errorMsg) {
                    workIt();
                }
            });
        });
        executor.execute(() -> {
            TencentAppCrawler.fetchAppNameFromPackage(pkg, new TencentAppCrawler.OnAppNameCallback() {
                @Override
                public void onSuccess(String appName, String iconUrl) {
                    Bean b = new Bean();
                    if (!TextUtils.isEmpty(appName)) {
                        b.setAppName(appName);
                    }
                    if (!TextUtils.isEmpty(iconUrl)) {
                        b.setAppIcon(iconUrl);
                    }
                    list.add(b);
                    workIt();
                }

                @Override
                public void onFail(String errorMsg) {
                    workIt();
                }
            });
        });
    }

    private static void workIt() {
        if (counter.decrementAndGet() > 0) return;
        for (int i = 0; i < list.size(); i++) {
            final Bean b = list.get(i);
            if (null == b || TextUtils.isEmpty(b.appName)) continue;
            if (!TextUtils.isEmpty(pg)) {
                map.put(pg, b);
            }
            MAIN_HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    callNameNoDesc(b, mCallback);
                    mCallback = null;
                }
            });
            return;
        }
        MAIN_HANDLER.post(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(pg)) {
                    map.put(pg, null);
                }
                mCallback.onFail();
                mCallback = null;
            }
        });
    }

    public interface Callback {
        void onSuccess(String appName, String appIcon);

        void onFail();
    }

    public static class Bean {
        String appName;
        String appIcon;

        public Bean() {
            this.appName = "";
            this.appIcon = "";
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getAppIcon() {
            return appIcon;
        }

        public void setAppIcon(String appIcon) {
            this.appIcon = appIcon;
        }
    }

    private static void callNameNoDesc(Bean b, Callback cb) {
        String s = b.appName;
        String k = "";
        if (s.contains(" - ")) {
            k = " - ";
        } else if (s.contains("-")) {
            k = "-";
        } else if (s.contains(" — ")) {
            k = " — ";
        } else if (s.contains("—")) {
            k = "—";
        }
        if (!TextUtils.isEmpty(k)) {
            String[] ag = s.split(k);
            if (ag.length > 0 && !TextUtils.isEmpty(ag[0])) {
                cb.onSuccess(ag[0], b.appIcon);
                return;
            }
        }
        cb.onSuccess(s, b.appIcon);
    }
}
