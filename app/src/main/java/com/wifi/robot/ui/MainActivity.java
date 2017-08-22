package com.wifi.robot.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.qihoo360.replugin.RePlugin;
import com.wifi.robot.R;
import com.wifi.robot.replugins.AdHelper;
import com.wifi.robot.replugins.EventHelper;
import com.wifi.robot.replugins.ReAdFactory;
import com.wifi.robot.replugins.ReNativeAdEvent;
import com.wifi.robot.utils.AssetsUtils;

import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {
    private static final int POI_FIRST = 1;
    private static final int POI_SECOND = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventHelper.register(this);
        //打开插件的开屏
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String config = AssetsUtils.readText(MainActivity.this, "ad_config.json");
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("adPlugin", "com.plugin.ad.LogoActivity"));
                    intent.putExtra("EXTRA_CONFIG", config);
                    intent.putExtra("EXTRA_POI", POI_FIRST);
                    RePlugin.startActivity(MainActivity.this, intent);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });

        //请求信息流广告
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 由于没有信息流广告的id,所以不做请求展示
                Toast.makeText(MainActivity.this, "请提供广告位ID", Toast.LENGTH_LONG).show();
                //String config = AssetsUtils.readText(MainActivity.this, "ad_config.json");
                //AdHelper.requestAd(config, POI_SECOND);
            }
        });
    }

    /**
     * 原生信息流
     * 插件广告请求
     *
     * @param event
     */
    @Subscribe
    public void onEventReADEvent(ReNativeAdEvent event) {
        if (event.isSucc) {
            //成功
            try {
                Object object = ReAdFactory.getNativeAD(POI_SECOND);
                //展示
                String title = (String) object.getClass().getMethod("getTitle").invoke(object);
                String iconUrl = (String) object.getClass().getMethod("getIconUrl").invoke(object);
                //调用曝光接口
                object.getClass().getMethod("onExposured", new Class[]{View.class}).invoke(object, findViewById(R.id.activity_main));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //失败
        }
    }

    /**
     * 点击原生信息流广告
     */
    private void clickNativeAD() {
        try {
            Object object = ReAdFactory.getNativeAD(POI_SECOND);
            //调用点击曝光接口
            object.getClass().getMethod("onClicked", new Class[]{View.class}).invoke(object, findViewById(R.id.activity_main));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventHelper.unRegister(this);
    }
}
