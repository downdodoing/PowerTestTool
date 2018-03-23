package com.meizu.flyme;

import android.app.Notification;

import java.lang.reflect.Field;

public class NotificationFlyme {

    public static void setFlymeNotificationIcon(Notification n, int resID) {
        try {
            Class<?> c = Notification.class;
            Class<?> cf = Class.forName("android.app.NotificationExt");
            Field field = c.getDeclaredField("mFlymeNotification");
            Field fieldf = cf.getDeclaredField("notificationIcon");
            Object object = field.get(n);
            fieldf.set(object, resID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
