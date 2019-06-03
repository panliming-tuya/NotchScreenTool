package com.smarx.notchlib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class ScreenUtil {

    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }


    /**
     * 获取contentView的高度，建议在onWindowFocusChanged之后调用，否则高度为0
     */
    public static int getContentViewHeight(Activity activity) {
        return getContentView(activity).getHeight();
    }

    /**
     * 获取contentView的在屏幕上的展示区域，建议在onWindowFocusChanged之后调用
     */
    public static Rect getContentViewDisplayFrame(Activity activity) {
        View contentView = getContentView(activity);
        Rect displayFrame = new Rect();
        contentView.getWindowVisibleDisplayFrame(displayFrame);
        return displayFrame;
    }

    public static View getContentView(Activity activity) {
        return activity.getWindow().getDecorView().findViewById(android.R.id.content);
    }


    /**
     * 获取navigationBar的高度
     */
    public static int getNavigationBarHeight(Context context) {
        return getDimensionPixel(context, "navigation_bar_height");
    }

    /**
     * 获取statusBar的高度
     */
    public static int getStatusBarHeight(Context context) {
        return getDimensionPixel(context, "status_bar_height");
    }


    private static int getDimensionPixel(Context context, String navigation_bar_height) {
        int result = 0;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(navigation_bar_height, "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取屏幕宽高
     */
    public static int[] getScreenSize(Context context) {
        int[] size = new int[2];
        WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        if (Build.VERSION.SDK_INT >= 17)
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                widthPixels = realSize.x;
                heightPixels = realSize.y;
            } catch (Throwable ignored) {
            }
        size[0] = widthPixels;
        size[1] = heightPixels;
        return size;
    }

    /**
     * 通过宽高计算notch的Rect
     *
     * @param notchWidth  刘海的宽
     * @param notchHeight 刘海的高
     */
    public static Rect calculateNotchRect(Context context, int notchWidth, int notchHeight) {
        int[] screenSize = getScreenSize(context);
        int screenWidth = screenSize[0];
        int screenHeight = screenSize[1];
        int left;
        int top;
        int right;
        int bottom;
        if (isPortrait(context)) {
            left = (screenWidth - notchWidth) / 2;
            top = 0;
            right = left + notchWidth;
            bottom = notchHeight;
        } else {
            left = 0;
            top = (screenHeight - notchWidth) / 2;
            right = notchHeight;
            bottom = top + notchWidth;
        }
        return new Rect(left, top, right, bottom);
    }
}
