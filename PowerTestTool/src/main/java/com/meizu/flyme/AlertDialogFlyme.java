package com.meizu.flyme;

import android.app.AlertDialog;

import java.lang.reflect.Method;

public class AlertDialogFlyme {

    public static void setButtonTextColor(AlertDialog dialog, int whichButton, int textColorId){
        try {
            Method method = AlertDialog.class.getDeclaredMethod("setButtonTextColor", int.class, int.class);
            method.setAccessible(true);
            method.invoke(dialog, whichButton,textColorId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
