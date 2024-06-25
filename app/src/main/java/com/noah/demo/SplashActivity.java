package com.noah.demo;

import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import com.noah.api.SplashAd;
import com.noah.replace.ISplashRewardListener;


public class SplashActivity extends Activity {
    private static final String TAG = "SplashShowActivity";

    private static final String SLOT = "10000186";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        final ViewGroup splashContainer = findViewById(R.id.splash_show_root);
        SplashAd.getAd(SplashActivity.this, splashContainer, SLOT, new SplashAd.AdListener() {
            @Override
            public void onAdError(com.noah.api.AdError error) {
                Log.i(TAG, "splash ad error code: " + error.getErrorCode() + " message: " + error.getErrorMessage());
                finish();
            }

            @Override
            public void onAdLoaded(SplashAd ad) {
                Log.i(TAG, "splash ad load");
                ad.showSplashAd(splashContainer);
            }

            @Override
            public void onAdShown(SplashAd ad) {
                Log.i(TAG, "splash ad show");
            }

            @Override
            public void onAdClicked(SplashAd ad) {
                Log.i(TAG, "splash ad clicked");
            }

            @Override
            public void onInterceptClick(final int i, final Map<String, String> map) {

            }

            @Override
            public void onAdSkip(final SplashAd splashAd) {
                Log.i(TAG, "splash ad skip");
            }

            @Override
            public void onAdTimeOver(final SplashAd splashAd) {
                Log.i(TAG, "splash ad timeover");
            }

            @Override
            public void onSplashLpShow(final boolean b) {
                Log.i(TAG, "splash ad onSplashLpShow");
            }

            @Override
            public void onAdExtraStat(final int i, final String s, final Map<String, String> map) {
                Log.i(TAG, "splash ad onAdExtraStat");
            }

            @Override
            public void onAdReward(@NonNull final ISplashRewardListener iSplashRewardListener) {
                Log.i(TAG, "splash ad onAdReward");
            }
        });

    }

}
