package com.meizu.flyme;

import android.content.Context;
import android.nfc.NfcAdapter;

import java.lang.reflect.Method;

public class NfcAdapterFlyme {

    public static boolean enable(Context context){
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        if (nfcAdapter == null) {
            return false;
        }
        try {
            Method method = nfcAdapter.getClass().getDeclaredMethod("enable");
            method.setAccessible(true);
            return (Boolean)method.invoke(nfcAdapter);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public static boolean disable(Context context){
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        if (nfcAdapter == null) {
            return false;
        }
        try {
            Method method = nfcAdapter.getClass().getDeclaredMethod("disable");
            method.setAccessible(true);
            return (Boolean)method.invoke(nfcAdapter);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
}
