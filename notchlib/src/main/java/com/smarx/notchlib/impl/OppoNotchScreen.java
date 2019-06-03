package com.smarx.notchlib.impl;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;

import com.smarx.notchlib.INotchScreen;
import com.smarx.notchlib.utils.ScreenUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;

@TargetApi(Build.VERSION_CODES.O)
public class OppoNotchScreen implements INotchScreen {

    /**
     * 获取刘海的坐标
     * <p>
     * 属性形如：[ro.oppo.screen.heteromorphism]: [378,0:702,80]
     * <p>
     * 获取到的值为378,0:702,80
     * <p>
     * <p>
     * (378,0)是刘海区域左上角的坐标
     * <p>
     * (702,80)是刘海区域右下角的坐标
     */
    private static String getScreenValue() {
        String value = "";
        Class<?> cls;
        try {
            cls = Class.forName("android.os.SystemProperties");
            Method get = cls.getMethod("get", String.class);
            Object object = cls.newInstance();
            value = (String) get.invoke(object, "ro.oppo.screen.heteromorphism");
        } catch (Throwable ignore) {
        }
        return value;
    }

    @Override
    public boolean hasNotch(Activity activity) {
        boolean ret = false;
        try {
            ret = activity.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
        } catch (Throwable ignore) {
        }
        return ret;
    }

    @Deprecated
    @Override
    public void setDisplayInNotch(Activity activity) {

    }

    @Override
    public void getNotchRect(Activity activity, NotchSizeCallback callback) {
        try {
            String screenValue = getScreenValue();
            if (!TextUtils.isEmpty(screenValue)) {
                String[] split = screenValue.split(":");
                String leftTopPoint = split[0];
                String[] leftAndTop = leftTopPoint.split(",");
                String rightBottomPoint = split[1];
                String[] rightAndBottom = rightBottomPoint.split(",");
                int left;
                int top;
                int right;
                int bottom;
                if (ScreenUtil.isPortrait(activity)) {
                    left = Integer.valueOf(leftAndTop[0]);
                    top = Integer.valueOf(leftAndTop[1]);
                    right = Integer.valueOf(rightAndBottom[0]);
                    bottom = Integer.valueOf(rightAndBottom[1]);
                } else {
                    left = Integer.valueOf(leftAndTop[1]);
                    top = Integer.valueOf(leftAndTop[0]);
                    right = Integer.valueOf(rightAndBottom[1]);
                    bottom = Integer.valueOf(rightAndBottom[0]);
                }
                Rect rect = new Rect(left, top, right, bottom);
                ArrayList<Rect> rects = new ArrayList<>();
                rects.add(rect);
                callback.onResult(rects);
            }
        } catch (Throwable ignore) {
            callback.onResult(null);
        }
    }
}
