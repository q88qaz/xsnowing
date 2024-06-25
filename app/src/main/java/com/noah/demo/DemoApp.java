package com.noah.demo;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.cretin.www.cretinautoupdatelibrary.model.TypeConfig;
import com.cretin.www.cretinautoupdatelibrary.model.UpdateConfig;
import com.cretin.www.cretinautoupdatelibrary.utils.AppUpdateUtils;
import com.cretin.www.cretinautoupdatelibrary.utils.SSLUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class DemoApp extends MultiDexApplication {
    @Override
    protected void attachBaseContext(final Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }


}
