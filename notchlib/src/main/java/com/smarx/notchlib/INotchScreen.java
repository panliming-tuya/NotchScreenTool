package com.smarx.notchlib;

import android.app.Activity;
import android.graphics.Rect;

import java.util.List;

public interface INotchScreen {

    boolean hasNotch(Activity activity);

    void setDisplayInNotch(Activity activity);

    void getNotchRect(Activity activity, NotchSizeCallback callback);

    interface NotchSizeCallback {
        void onResult(List<Rect> notchRects);
    }

    interface HasNotchCallback {
        void onResult(boolean hasNotch);
    }

    interface NotchScreenCallback {
        void onResult(NotchScreenInfo notchScreenInfo);
    }

    class NotchScreenInfo {
        public boolean hasNotch;
        public List<Rect> notchRects;
    }
}
