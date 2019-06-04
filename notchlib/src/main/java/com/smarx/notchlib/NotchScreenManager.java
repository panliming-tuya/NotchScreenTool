package com.smarx.notchlib;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;

import com.smarx.notchlib.impl.AndroidPNotchScreen;
import com.smarx.notchlib.impl.HuaweiNotchScreen;
import com.smarx.notchlib.impl.MiNotchScreen;
import com.smarx.notchlib.impl.OppoNotchScreen;
import com.smarx.notchlib.utils.RomUtils;

import java.util.List;

public class NotchScreenManager {

    private static final NotchScreenManager instance = new NotchScreenManager();
    private final INotchScreen notchScreen;

    private NotchScreenManager() {
        notchScreen = getNotchScreen();
    }

    public static NotchScreenManager getInstance() {
        return instance;
    }

    public void setDisplayInNotch(Activity activity) {
        if (notchScreen != null)
            notchScreen.setDisplayInNotch(activity);
    }

    public void getNotchInfo(final Activity activity, final INotchScreen.NotchScreenCallback notchScreenCallback) {
        final INotchScreen.NotchScreenInfo notchScreenInfo = new INotchScreen.NotchScreenInfo();
        if (notchScreen != null && notchScreen.hasNotch(activity)) {
            notchScreen.getNotchRect(activity, new INotchScreen.NotchSizeCallback() {
                @Override
                public void onResult(List<Rect> notchRects) {
                    if (notchRects != null && notchRects.size() > 0) {
                        notchScreenInfo.hasNotch = true;
                        notchScreenInfo.notchRects = notchRects;
                    }
                    notchScreenCallback.onResult(notchScreenInfo);
                }
            });
        } else {
            notchScreenCallback.onResult(notchScreenInfo);
        }
    }

    private INotchScreen getNotchScreen() {
        INotchScreen notchScreen = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            notchScreen = new AndroidPNotchScreen();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (RomUtils.isHuawei()) {
                notchScreen = new HuaweiNotchScreen();
            } else if (RomUtils.isOppo()) {
                notchScreen = new OppoNotchScreen();
            } else if (RomUtils.isVivo()) {
                notchScreen = new HuaweiNotchScreen();
            } else if (RomUtils.isXiaomi()) {
                notchScreen = new MiNotchScreen();
            }
        }
        return notchScreen;
    }
}
