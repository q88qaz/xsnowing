package com.vise.utils.market;

import android.text.TextUtils;

public class MarBean {
    String appName;
    String appIcon;
    String pkg;

    public MarBean() {
        this.appName = "";
        this.appIcon = "";
        this.pkg = "";
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = cutName(appName);
    }

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    private static String cutName(String s) {
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
                return ag[0];
            }
        }
        return s;
    }
}
