package com.smarx.notchscreenutil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openLandscapeActivity(View view) {
        startActivity(new Intent(this, LandscapeActivity.class));
    }

    public void openPortraitActivity(View view) {
        startActivity(new Intent(this, PortraitActivity.class));
    }
}
