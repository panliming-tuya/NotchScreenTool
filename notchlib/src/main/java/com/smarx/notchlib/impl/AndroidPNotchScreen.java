package com.smarx.notchlib.impl;

import android.annotation.TargetApi;
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

@TargetApi(Build.VERSION_CODES.P)
public class AndroidPNotchScreen implements INotchScreen {

    /**
     * Android P 没有单独的判断方法，根据getNotchRect方法的返回结果处理即可
     */
    @Override
    public boolean hasNotch(Activity activity) {
        return true;
    }

    @Override
    public void setDisplayInNotch(Activity activity) {
        Window window = activity.getWindow();
        // 延伸显示区域到耳朵区
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        window.setAttributes(lp);
        // 允许内容绘制到耳朵区
        final View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    @Override
    public void getNotchRect(Activity activity, final NotchSizeCallback callback) {
        final View contentView = activity.getWindow().getDecorView();
        contentView.post(new Runnable() {
            @Override
            public void run() {
                WindowInsets windowInsets = contentView.getRootWindowInsets();
                if (windowInsets != null) {
                    DisplayCutout cutout = windowInsets.getDisplayCutout();
                    if (cutout != null) {
                        List<Rect> rects = cutout.getBoundingRects();
                        callback.onResult(rects);
                        return;
                    }
                }
                callback.onResult(null);
            }
        });
    }
}
