package com.noah.demo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.noah.api.AdError;
import com.noah.api.AdIconView;
import com.noah.api.BiddingInfo;
import com.noah.api.BiddingInfoList;
import com.noah.api.BiddingLossReason;
import com.noah.api.MediaView;
import com.noah.api.NativeAd;
import com.noah.api.NativeAdView;
import com.noah.api.NoahAd;
import com.noah.api.RewardedVideoAd;

public class NativeAdActivity extends AppCompatActivity {

    private static final String SLOT = "10000187";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_ad);
        setTitle("NativeAd");

        final TextView tipsView = findViewById(R.id.nativead_tips);

        findViewById(R.id.nativead_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                tipsView.setText("loading......");
                if (NativeAd.isReady(SLOT)) {
                    tipsView.setText("get ad from cache..."); //获取缓存池中的广告
                } else {
                    tipsView.setText("get ad from adn..."); //实时请求adn获取广告
                }
                NativeAd.getAd(NativeAdActivity.this, SLOT, new NativeAd.AdListener() {
                    @Override
                    public void onAdError(final AdError adError) {
                        tipsView.setText("load error, error code:" + adError.getErrorCode() + " error message: " + adError.getErrorMessage());
                    }

                    @Override
                    public void onAdLoaded(final NativeAd ad) {
                        tipsView.setText("load success.");
                        LinearLayout container = findViewById(R.id.nativead_container);
                        container.removeAllViews();
                        final NativeAdView nativeAdView = new NativeAdView(NativeAdActivity.this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(NativeAdActivity.dip2px(NativeAdActivity.this, 320), ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.gravity = Gravity.CENTER_HORIZONTAL;
                        container.addView(nativeAdView, params);

                        View customView = LayoutInflater.from(NativeAdActivity.this).inflate(R.layout.native_ad, null);
                        nativeAdView.setCustomView(customView);
                        nativeAdView.setNativeAd(ad);
                        nativeAdView.setVisibility(View.VISIBLE);

                        Button cta = customView.findViewById(R.id.demo_native_ad_call_to_action);
                        TextView title = customView.findViewById(R.id.demo_native_ad_title);
                        TextView content = customView.findViewById(R.id.demo_native_ad_content);
                        AdIconView iconView = customView.findViewById(R.id.demo_native_ad_icon);
                        MediaView coverView = customView.findViewById(R.id.demo_native_ad_media_view);

                        ImageView closeView = customView.findViewById(R.id.icon_close);
                        if (closeView != null) {
                            closeView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    nativeAdView.setVisibility(View.GONE);
                                }
                            });
                        }

                        NativeAd.NativeAssets assets = ad.getAdAssets();
                        if (assets != null) {
                            cta.setText(assets.getCallToAction());
                            title.setText(assets.getTitle());
                            content.setText(assets.getDescription());
                            iconView.setNativeAd(ad);
                            coverView.setNativeAd(ad);

                            List<View> clickViewList = new ArrayList<>();
                            clickViewList.add(cta);
                            clickViewList.add(title);
                            clickViewList.add(content);
                            clickViewList.add(iconView);
                            clickViewList.add(coverView);

                            List<View> directDownloadViewList = new ArrayList<>();
                            //directDownloadViewList.add(cta); //直接下载View, 点击此View SDK不弹六要素弹窗

                            ad.registerViewForInteraction(nativeAdView, clickViewList, null, directDownloadViewList);
                        }

                    }

                    @Override
                    public void onAdLoaded(final List<NativeAd> list) {

                    }

                    @Override
                    public void onAdShown(final NativeAd ad) {
                        tipsView.setText("show success.");
                        sendWindNotification(ad);
                    }

                    @Override
                    public void onAdClosed(final NativeAd ad) {

                    }

                    @Override
                    public void onAdClicked(final NativeAd ad) {
                        sendLossNotification(ad);

                    }

                    @Override
                    public void onDownloadStatusChanged(final NativeAd nativeAd, final int i) {

                    }

                    @Override
                    public void onAdEvent(final NativeAd ad, final int eventId, final Object extInfo) {

                    }
                });
            }
        });
    }

    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private static void sendWindNotification(final NoahAd ad) {
        //ADN 1 相关信息回传
        BiddingInfo biddingBean = BiddingInfo.newBuilder().setAdnName("huichuan")       // ADN名称
                                             .setAdId("adid123")                        // 胜出ADN的adid（如有）
                                             .setPrice(9)                               // ADN当前Bid价格（分）
                                             .setBidType("RT")                          // BidType
                                             .setPlacementId("placementId456")          // placementId
                                             .setResult(BiddingInfo.WIN)                // ADN竞胜状态（胜/负)
                                             .build();
        //ADN 2 相关信息回传
        BiddingInfo biddingBean2 = BiddingInfo.newBuilder().setAdnName("gdt")
                                              .setAdId("adid1111")
                                              .setPrice(2)
                                              .setBidType("RT")
                                              .setResult(BiddingInfo.LOSS)
                                              .build();

        //支持回传多个ADN信息
        BiddingInfoList list = BiddingInfoList.newBuilder()
                                              .add(biddingBean)
                                              .add(biddingBean2)
                                              .build();
        ad.sendNotification(false, 2, 0, list);

    }

    private static void sendLossNotification(final NoahAd ad) {
        BiddingInfo biddingBean = BiddingInfo.newBuilder()
                                             .setAdnName("hc")
                                             .setPlacementId("placementId456")
                                             .setAdId("adid456")
                                             .setBidType("RT")
                                             .setPrice(9)
                                             .setResult(BiddingInfo.LOSS)
                                             .build();

        BiddingInfoList list = BiddingInfoList.newBuilder()
                                              .add(biddingBean)
                                              .build();
        ad.sendNotification(false, 1, BiddingLossReason.LOW_PRICE, list);
    }
}
