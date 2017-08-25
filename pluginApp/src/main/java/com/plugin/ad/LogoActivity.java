package com.plugin.ad;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.plugin.ad.listeners.ISplashLoadListener;
import com.plugin.ad.managers.LoadManager;

public class LogoActivity extends AppCompatActivity {
    private ViewGroup container;
    private TextView skipView;
    public boolean canJump = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_logo);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canJump) {
            next();
        }
        canJump = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        canJump = false;
    }

    private void initView() {
        container = (ViewGroup) this.findViewById(R.id.splash_container);
        skipView = (TextView) findViewById(R.id.skip_view);
    }

    private void initData() {
        String config = getIntent().getStringExtra("EXTRA_CONFIG");
        int poi = getIntent().getIntExtra("EXTRA_POI", 1);
        requestSplash(config, poi);
    }

    /**
     * 请求开屏广告
     *
     * @param config
     * @param poi
     */
    private void requestSplash(String config, int poi) {
        LoadManager.requestSplashAD(LogoActivity.this, container, skipView, config, poi, new ISplashLoadListener() {
            @Override
            public void onADDismissed() {
                next();
            }

            @Override
            public void onADPresent(boolean isGdt) {
                container.setVisibility(View.VISIBLE);
                if (isGdt) {
                    skipView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onADClicked(boolean isGdt) {

            }

            @Override
            public void onADTick(long var1) {
                skipView.setText("" + Math.round(var1 / 1000f));
            }

            @Override
            public void onAdFailed(String var1) {
                intoMainPage();
            }
        });
    }

    /**
     * 下一步
     */
    private void next() {
        if (canJump) {
            intoMainPage();
        } else {
            canJump = true;
        }
    }

    /**
     * 进入主程序
     */
    private void intoMainPage() {
        try {
            //TODO 打开宿主应用
            Intent intent = new Intent();
            intent.setClassName("com.wifi.robot", "com.wifi.robot.ui.SecondActivity");
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
