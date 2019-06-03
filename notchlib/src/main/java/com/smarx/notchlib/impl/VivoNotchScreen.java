package com.smarx.notchlib.impl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;

import com.smarx.notchlib.INotchScreen;
import com.smarx.notchlib.utils.ScreenUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class VivoNotchScreen implements INotchScreen {
    public static boolean isNotch() {
        boolean value = false;
        int mask = 0x00000020;
        try {
            Class<?> cls = Class.forName("android.util.FtFeature");
            Method hideMethod = cls.getMethod("isFtFeatureSupport", int.class);
            Object object = cls.newInstance();
            value = (boolean) hideMethod.invoke(object, mask);
        } catch (Exception e) {
            Log.e("tag", "get error() ", e);
        }
        return value;
    }

    /**
     * vivo的适配文档中就告诉是27dp，未告知如何动态获取
     */
    public static int getNotchHeight(Context context) {
        float density = getDensity(context);
        return (int) (27 * density);
    }

    /**
     * vivo的适配文档中就告诉是100dp，未告知如何动态获取
     */
    public static int getNotchWidth(Context context) {
        float density = getDensity(context);
        return (int) (100 * density);
    }

    private static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    @Override
    public void hasNotch(Activity activity, HasNotchCallback callback) {
        callback.onResult(isNotch());
    }

    @Override
    public void setDisplayInNotch(Activity activity) {

    }

    @Override
    public void getNotchRect(Activity activity, NotchSizeCallback callback) {
        ArrayList<Rect> rects = new ArrayList<>();
        Rect rect = ScreenUtil.calculateNotchRect(activity, getNotchWidth(activity), getNotchHeight(activity));
        rects.add(rect);
        callback.onResult(rects);
    }
}
