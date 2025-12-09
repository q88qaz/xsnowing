package com.vise.utils.market;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class AppNameFinder {

    private static final int NUM = 3;
    private static final List<MarBean> list = Collections.synchronizedList(new ArrayList<>());
    private static final Map<String, MarBean> map = Collections.synchronizedMap(new ArrayMap<>());
    // 主线程Handler，用于UI线程回调
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());
    private static String pg = "";
    private static Callback mCallback;
    private static AtomicInteger counter;
    private static ExecutorService executor;
    // 多包名查询
    private static MultCallback multCallback;
    private static boolean isHighQuality = false;
    private static final Map<String, MarBean> beanMap = Collections.synchronizedMap(new ArrayMap<>());
    private static final List<String> seekList = Collections.synchronizedList(new ArrayList<>());

    public static void setHighQuality(boolean b) {
        if (b == isHighQuality) {
            return;
        }
        isHighQuality = b;
        try {
            list.clear();
            map.clear();
            beanMap.clear();
            seekList.clear();
        } catch (Throwable t) {

        }
    }

    public static void searchApp(String s, final Callback cb) {
        if (isNoStr(s)) {
            cb.onFail();
            return;
        }
        final String pkg = s.trim();
        if (map.containsKey(pkg)) {
            MarBean b = map.get(pkg);
            if (null != b) {
                cb.onSuccess(b.appName, b.appIcon);
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
                    MarBean b = new MarBean();
                    if (!TextUtils.isEmpty(appName)) {
                        b.setAppName(appName);
                    }
                    if (!TextUtils.isEmpty(iconUrl)) {
                        b.setAppIcon(iconUrl);
                    }
                    if (isHighQuality) {
                        list.add(b);
                    } else {
                        list.add(0, b);
                    }
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
                    MarBean b = new MarBean();
                    if (!TextUtils.isEmpty(appName)) {
                        b.setAppName(appName);
                    }
                    if (!TextUtils.isEmpty(iconUrl)) {
                        b.setAppIcon(iconUrl);
                    }
                    if (isHighQuality) {
                        list.add(0, b);
                    } else {
                        list.add(b);
                    }
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
                    MarBean b = new MarBean();
                    if (!TextUtils.isEmpty(appName)) {
                        b.setAppName(appName);
                    }
                    if (!TextUtils.isEmpty(iconUrl)) {
                        b.setAppIcon(iconUrl);
                    }
                    if (isHighQuality) {
                        list.add(0, b);
                    } else {
                        list.add(b);
                    }
                    workIt();
                }

                @Override
                public void onFail(String errorMsg) {
                    workIt();
                }
            });
        });
    }

    public static void searchApp(String[] args, final MultCallback cb) {
        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(args));
        searchApp(list, cb);
    }

    public static void searchApp(List<String> list, final MultCallback cb) {
        if (null == list || list.isEmpty()) {
            cb.onFail();
            return;
        }
        if (null != multCallback) {
            return;
        }
        multCallback = cb;
        beanMap.clear();
        seekList.clear();
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            if (isNoStr(str)) {
                continue;
            }
            final String s = str.trim();
            if (map.containsKey(s)) {
                MarBean b = map.get(s);
                if (null == b) {
                    b = new MarBean();
                }
                b.setPkg(s);
                beanMap.put(s, b);
            } else {
                seekList.add(s);
            }
        }
        if (list.size() == beanMap.size()) {
            multCallback.onSuccess(beanMap);
            multCallback = null;
            return;
        }

        final int size = seekList.size() * NUM;
        if (size == 0) {
            if (beanMap.isEmpty()) {
                multCallback.onFail();
            } else {
                multCallback.onSuccess(beanMap);
            }
            multCallback = null;
            return;
        }
        counter = new AtomicInteger(size);
        executor = Executors.newFixedThreadPool(size);
        for (int i = 0; i < seekList.size(); i++) {
            final String pkg = seekList.get(i);
            executor.execute(() -> {
                XiaomiAppCrawler.getAppNameAsync(pkg, new XiaomiAppCrawler.OnAppNameCallback() {
                    @Override
                    public void onSuccess(String appName, String iconUrl) {
                        MarBean b = new MarBean();
                        if (!TextUtils.isEmpty(appName)) {
                            b.setAppName(appName);
                        }
                        if (!TextUtils.isEmpty(iconUrl)) {
                            b.setAppIcon(iconUrl);
                        }
                        if (isHighQuality) {
                            if (!map.containsKey(pkg)) {
                                map.put(pkg, b);
                            }
                        } else {
                            map.put(pkg, b);
                        }
                        workEt();
                    }

                    @Override
                    public void onFail(String errorMsg) {
                        workEt();
                    }
                });
            });
            executor.execute(() -> {
                MeizuAppCrawler.getAppNameAsync(pkg, new MeizuAppCrawler.OnAppNameCallback() {
                    @Override
                    public void onSuccess(String appName, String iconUrl) {
                        MarBean b = new MarBean();
                        if (!TextUtils.isEmpty(appName)) {
                            b.setAppName(appName);
                        }
                        if (!TextUtils.isEmpty(iconUrl)) {
                            b.setAppIcon(iconUrl);
                        }
                        if (isHighQuality) {
                            map.put(pkg, b);
                        } else if (!map.containsKey(pkg)) {
                            map.put(pkg, b);
                        }
                        workEt();
                    }

                    @Override
                    public void onFail(String errorMsg) {
                        workEt();
                    }
                });
            });
            executor.execute(() -> {
                TencentAppCrawler.fetchAppNameFromPackage(pkg, new TencentAppCrawler.OnAppNameCallback() {
                    @Override
                    public void onSuccess(String appName, String iconUrl) {
                        MarBean b = new MarBean();
                        if (!TextUtils.isEmpty(appName)) {
                            b.setAppName(appName);
                        }
                        if (!TextUtils.isEmpty(iconUrl)) {
                            b.setAppIcon(iconUrl);
                        }
                        if (isHighQuality) {
                            map.put(pkg, b);
                        } else if (!map.containsKey(pkg)) {
                            map.put(pkg, b);
                        }
                        workEt();
                    }

                    @Override
                    public void onFail(String errorMsg) {
                        workEt();
                    }
                });
            });
        }

    }

    private static void workIt() {
        if (counter.decrementAndGet() > 0) return;
        for (int i = 0; i < list.size(); i++) {
            final MarBean b = list.get(i);
            if (null == b || TextUtils.isEmpty(b.appName)) continue;
            if (!TextUtils.isEmpty(pg)) {
                map.put(pg, b);
            }
            MAIN_HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onSuccess(b.appName, b.appIcon);
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

    private static void workEt() {
        if (counter.decrementAndGet() > 0) return;
        for (int i = 0; i < seekList.size(); i++) {
            String s = seekList.get(i);
            MarBean b = null;
            if (map.containsKey(s)) {
                b = map.get(s);
            }
            if (null == b) {
                map.put(s, null);
                b = new MarBean();
            }
            b.setPkg(s);
            beanMap.put(s, b);
        }
        if (beanMap.isEmpty()) {
            if (null != multCallback) {
                multCallback.onFail();
                multCallback = null;
            }
            return;
        }
        if (null != multCallback) {
            multCallback.onSuccess(beanMap);
            multCallback = null;
        }
    }

    public interface Callback {
        void onSuccess(String appName, String appIcon);

        void onFail();
    }

    public interface MultCallback {
        void onSuccess(Map<String, MarBean> map);

        void onFail();
    }

    private static boolean isNoStr(String s) {
        return TextUtils.isEmpty(s) || TextUtils.isEmpty(s.trim());
    }

}
