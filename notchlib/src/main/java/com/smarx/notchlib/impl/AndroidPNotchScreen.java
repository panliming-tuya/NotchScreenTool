package com.smarx.notchlib.impl;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import com.smarx.notchlib.INotchScreen;

import java.util.List;

public class AndroidPNotchScreen implements INotchScreen {

    @Override
    public void hasNotch(Activity activity, final HasNotchCallback callback) {
        View contentView = activity.getWindow().getDecorView().findViewById(android.R.id.content).getRootView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            contentView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                @Override
                public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                    DisplayCutout cutout = windowInsets.getDisplayCutout();
                    callback.onResult(cutout != null);
                    return windowInsets;
                }
            });
        }
    }

    @Override
    public void setDisplayInNotch(Activity activity) {
        Window window = activity.getWindow();
        // 延伸显示区域到耳朵区
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            window.setAttributes(lp);
        }
        // 允许内容绘制到耳朵区
        final View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    @Override
    public void getNotchRect(Activity activity, final NotchSizeCallback callback) {
        View contentView = activity.getWindow().getDecorView().findViewById(android.R.id.content).getRootView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            contentView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                @Override
                public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                    DisplayCutout cutout = windowInsets.getDisplayCutout();
                    if (cutout != null) {
                        List<Rect> rects = cutout.getBoundingRects();
                        callback.onResult(rects);
                    }
                    return windowInsets;
                }
            });
        }
    }
}
