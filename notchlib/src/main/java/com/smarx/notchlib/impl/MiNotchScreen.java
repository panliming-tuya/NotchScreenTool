package com.smarx.notchlib.impl;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.Window;

import com.smarx.notchlib.INotchScreen;
import com.smarx.notchlib.utils.ScreenUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;

@TargetApi(Build.VERSION_CODES.O)
public class MiNotchScreen implements INotchScreen {
    private static boolean isNotch() {
        try {
            Method getInt = Class.forName("android.os.SystemProperties").getMethod("getInt", String.class, int.class);
            int notch = (int) getInt.invoke(null, "ro.miui.notch", 0);
            return notch == 1;
        } catch (Throwable ignore) {
        }
        return false;
    }

    public static int getNotchHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("notch_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getNotchWidth(Context context) {
        int resourceId = context.getResources().getIdentifier("notch_width", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    @Override
    public boolean hasNotch(Activity activity) {
        return isNotch();
    }

    @Override
    public void setDisplayInNotch(Activity activity) {
        int flag = 0x00000100 | 0x00000200 | 0x00000400;
        try {
            Method method = Window.class.getMethod("addExtraFlags",
                    int.class);
            method.invoke(activity.getWindow(), flag);
        } catch (Exception ignore) {
        }
    }

    @Override
    public void getNotchRect(Activity activity, NotchSizeCallback callback) {
        Rect rect = ScreenUtil.calculateNotchRect(activity, getNotchWidth(activity), getNotchHeight(activity));
        ArrayList<Rect> rects = new ArrayList<>();
        rects.add(rect);
        callback.onResult(rects);
    }
}
