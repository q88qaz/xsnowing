package com.noah.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.noah.api.AdError;
import com.noah.api.RewardedVideoAd;

public class RewardedVideoAdActivity extends AppCompatActivity {

    private static final String SLOT = "10000184";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewarded_video_ad);
        setTitle("RewardedVideoAd");

        final TextView tipsView = findViewById(R.id.rewardedvideoad_tips);

        findViewById(R.id.rewardedvideoad_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                tipsView.setText("loading......");
                RewardedVideoAd.getAd(RewardedVideoAdActivity.this, SLOT, new RewardedVideoAd.AdListener() {
                    @Override
                    public void onAdError(final AdError adError) {
                        tipsView.setText("load error, error code:" + adError.getErrorCode() + " error message: " + adError.getErrorMessage());
                    }

                    @Override
                    public void onAdLoaded(final RewardedVideoAd ad) {
                        tipsView.setText("load success.");
                        ad.show();
                    }

                    @Override
                    public void onAdShown(final RewardedVideoAd ad) {
                        tipsView.setText("show success.");
                    }

                    @Override
                    public void onAdClosed(final RewardedVideoAd ad) {

                    }

                    @Override
                    public void onAdClicked(final RewardedVideoAd ad) {

                    }

                    @Override
                    public void onVideoStart(final RewardedVideoAd ad) {

                    }

                    @Override
                    public void onVideoEnd(final RewardedVideoAd ad) {

                    }

                    @Override
                    public void onRewarded(final RewardedVideoAd ad) {

                    }
                });
            }
        });
    }
}
