package com.plugin.ad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.plugin.ad.utils.AssetsUtils;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, LogoActivity.class);
        intent.putExtra("EXTRA_CONFIG", AssetsUtils.readText(MainActivity.this, "ad_config.json"));
        intent.putExtra("EXTRA_POI", 1);
        startActivity(intent);
    }
}
