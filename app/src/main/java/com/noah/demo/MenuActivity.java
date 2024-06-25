package com.noah.demo;

import java.net.NetworkInterface;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.noah.api.GlobalConfig;
import com.noah.api.NoahSdk;
import com.noah.api.NoahSdkConfig;
import com.noah.sdk.dg.NoahAdnActivity;

public class MenuActivity extends AppCompatActivity {
    private static final String TAG = "MenuActivity";

    private static final String APP_KEY = "10059";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Demo");
        setContentView(R.layout.activity_bidding);

        initNoahSdk();

        findViewById(R.id.nativead).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startActivity(new Intent(MenuActivity.this, NativeAdActivity.class));
            }
        });

        findViewById(R.id.reward).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startActivity(new Intent(MenuActivity.this, RewardedVideoAdActivity.class));
            }
        });

        findViewById(R.id.splash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(MenuActivity.this, SplashActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(MenuActivity.this, NoahAdnActivity.class);
                startActivity(intent);
            }
        });


    }

    private void initNoahSdk() {
        NoahSdkConfig config = new NoahSdkConfig.Builder()
                                 .setAppKey(APP_KEY)
                                 .setOuterSettings(new NoahSdkConfig.NoahOuterSettings() {
                                     /**
                                      * 强烈建议返回oaid，确实oaid会严重影响广告效果
                                      */
                                    @Override
                                    public String getOAID() {
                                        Log.d(TAG, "call getOAID.");
                                        return "oaid"; //强烈建议返回oaid，确实oaid会严重影响广告效果
                                    }

                                     @Override
                                     public String getOAID2() {
                                         return "oaid2";
                                     }

                                     /**
                                      * 媒体接管 TelephonyManager.getDeviceId()接口调用，如果媒体实现了这个接口，SDK不会再主动调用系统接口
                                      * @param telephonyManager
                                      * @return
                                      */
                                     @Override
                                     public String getDeviceId(final TelephonyManager telephonyManager) {
                                         Log.d(TAG, "call getDeviceId.");
                                         return "";
                                     }

                                     /**
                                      * 媒体接管 TelephonyManager.getDeviceId(int slotIndex)接口调用，如果媒体实现了这个接口，SDK不会再主动调用系统接口
                                      * @param telephonyManager
                                      * @param i
                                      * @return
                                      */
                                     @Override
                                     public String getDeviceId(final TelephonyManager telephonyManager, final int i) {
                                         Log.d(TAG, "call getDeviceId slotIndex.");
                                         return "";
                                     }

                                     /**
                                      * 媒体接管 WifiInfo.getMacAddress()接口调用，如果媒体实现了这个接口，SDK不会再主动调用系统接口
                                      * @param wifiInfo
                                      * @return
                                      */
                                     @Override
                                     public String getMacAddress(final WifiInfo wifiInfo) {
                                         Log.d(TAG, "call getMacAddress.");
                                         return "";
                                     }

                                     /**
                                      * 媒体接管 NetworkInterface.getHardwareAddress()接口调用，如果媒体实现了这个接口，SDK不会再主动调用系统接口
                                      * @param networkInterface
                                      * @return
                                      */
                                     @Override
                                     public byte[] getHardwareAddress(final NetworkInterface networkInterface) {
                                         Log.d(TAG, "call getHardwareAddress.");
                                         return null;
                                     }

                                     /**
                                      *  媒体接管 TelephonyManager.getImei(int slotIndex) 接口调用，如果媒体实现了这个接口，SDK不会再主动调用系统接口
                                      * @param telephonyManager
                                      * @param i
                                      * @return
                                      */
                                     @Override
                                     public String getImei(final TelephonyManager telephonyManager, final int i) {
                                         Log.d(TAG, "call getImei slotIndex.");
                                         return "";
                                     }

                                     /**
                                      * 媒体接管 TelephonyManager.getImei() 接口调用，如果媒体实现了这个接口，SDK不会再主动调用系统接口
                                      * @param telephonyManager
                                      * @return
                                      */
                                     @Override
                                     public String getImei(final TelephonyManager telephonyManager) {
                                         Log.d(TAG, "call getImei.");
                                         return "";
                                     }
                                 })
                                 .build();

        GlobalConfig globalConfig = GlobalConfig.newBuilder()
                                                .setDebug(true) //需要看SDK日志可以打开这个开关，正式包务必设置为false
                                                .build();
        NoahSdk.init(this.getApplication(), config, globalConfig);
    }

}
