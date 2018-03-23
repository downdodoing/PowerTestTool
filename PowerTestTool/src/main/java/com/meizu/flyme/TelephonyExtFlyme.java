
package com.meizu.flyme;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TelephonyExtFlyme {
    private static final String TAG = "TelephonyExtFlyme";

    /* NETWORK_MODE_* See ril.h RIL_REQUEST_SET_PREFERRED_NETWORK_TYPE */
    public static int NETWORK_MODE_WCDMA_PREF;              /*= 0; /* GSM/WCDMA (WCDMA preferred) */
    public static int NETWORK_MODE_GSM_ONLY;                /*= 1; /* GSM only */
    public static int NETWORK_MODE_WCDMA_ONLY;              /*= 2; /* WCDMA only */
    public static int NETWORK_MODE_GSM_UMTS;                /*= 3; /* GSM/WCDMA (auto mode, according to PRL)
                                                            AVAILABLE Application Settings menu*/
    public static int NETWORK_MODE_CDMA;                    /*= 4; /* CDMA and EvDo (auto mode, according to PRL)
                                                            AVAILABLE Application Settings menu*/
    public static int NETWORK_MODE_CDMA_NO_EVDO;            /*= 5; /* CDMA only */
    public static int NETWORK_MODE_EVDO_NO_CDMA;            /*= 6; /* EvDo only */
    public static int NETWORK_MODE_GLOBAL;                  /*= 7; /* GSM/WCDMA, CDMA, and EvDo (auto mode, according to PRL)
                                                            AVAILABLE Application Settings menu*/
    public static int NETWORK_MODE_LTE_CDMA_EVDO;           /*= 8; /* LTE, CDMA and EvDo */
    public static int NETWORK_MODE_LTE_GSM_WCDMA;           /*= 9; /* LTE, GSM/WCDMA */
    public static int NETWORK_MODE_LTE_CDMA_EVDO_GSM_WCDMA; /*= 10; /* LTE, CDMA, EvDo, GSM/WCDMA */
    public static int NETWORK_MODE_LTE_ONLY;                /*= 11; /* LTE Only mode. */
    public static int NETWORK_MODE_LTE_WCDMA;               /*= 12; /* LTE/WCDMA */
    static {

        try {
            Class<?> delegate = Class.forName("com.android.internal.telephony.RILConstants");
            Field field = delegate.getDeclaredField("NETWORK_MODE_WCDMA_PREF");
            field.setAccessible(true);
            NETWORK_MODE_WCDMA_PREF = (int) field.get(delegate);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            Class<?> delegate = Class.forName("com.android.internal.telephony.RILConstants");
            Field field = delegate.getDeclaredField("NETWORK_MODE_GSM_ONLY");
            field.setAccessible(true);
            NETWORK_MODE_GSM_ONLY = (int) field.get(delegate);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            Class<?> delegate = Class.forName("com.android.internal.telephony.RILConstants");
            Field field = delegate.getDeclaredField("NETWORK_MODE_WCDMA_ONLY");
            field.setAccessible(true);
            NETWORK_MODE_WCDMA_ONLY = (int) field.get(delegate);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            Class<?> delegate = Class.forName("com.android.internal.telephony.RILConstants");
            Field field = delegate.getDeclaredField("NETWORK_MODE_GSM_UMTS");
            field.setAccessible(true);
            NETWORK_MODE_GSM_UMTS = (int) field.get(delegate);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            Class<?> delegate = Class.forName("com.android.internal.telephony.RILConstants");
            Field field = delegate.getDeclaredField("NETWORK_MODE_CDMA");
            field.setAccessible(true);
            NETWORK_MODE_CDMA = (int) field.get(delegate);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            Class<?> delegate = Class.forName("com.android.internal.telephony.RILConstants");
            Field field = delegate.getDeclaredField("NETWORK_MODE_CDMA_NO_EVDO");
            field.setAccessible(true);
            NETWORK_MODE_CDMA_NO_EVDO = (int) field.get(delegate);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            Class<?> delegate = Class.forName("com.android.internal.telephony.RILConstants");
            Field field = delegate.getDeclaredField("NETWORK_MODE_EVDO_NO_CDMA");
            field.setAccessible(true);
            NETWORK_MODE_EVDO_NO_CDMA = (int) field.get(delegate);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            Class<?> delegate = Class.forName("com.android.internal.telephony.RILConstants");
            Field field = delegate.getDeclaredField("NETWORK_MODE_GLOBAL");
            field.setAccessible(true);
            NETWORK_MODE_GLOBAL = (int) field.get(delegate);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            Class<?> delegate = Class.forName("com.android.internal.telephony.RILConstants");
            Field field = delegate.getDeclaredField("NETWORK_MODE_LTE_CDMA_EVDO");
            field.setAccessible(true);
            NETWORK_MODE_LTE_CDMA_EVDO = (int) field.get(delegate);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            Class<?> delegate = Class.forName("com.android.internal.telephony.RILConstants");
            Field field = delegate.getDeclaredField("NETWORK_MODE_LTE_GSM_WCDMA");
            field.setAccessible(true);
            NETWORK_MODE_LTE_GSM_WCDMA = (int) field.get(delegate);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            Class<?> delegate = Class.forName("com.android.internal.telephony.RILConstants");
            Field field = delegate.getDeclaredField("NETWORK_MODE_LTE_CDMA_EVDO_GSM_WCDMA");
            field.setAccessible(true);
            NETWORK_MODE_LTE_CDMA_EVDO_GSM_WCDMA = (int) field.get(delegate);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            Class<?> delegate = Class.forName("com.android.internal.telephony.RILConstants");
            Field field = delegate.getDeclaredField("NETWORK_MODE_LTE_ONLY");
            field.setAccessible(true);
            NETWORK_MODE_LTE_ONLY = (int) field.get(delegate);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            Class<?> delegate = Class.forName("com.android.internal.telephony.RILConstants");
            Field field = delegate.getDeclaredField("NETWORK_MODE_LTE_WCDMA");
            field.setAccessible(true);
            NETWORK_MODE_LTE_WCDMA = (int) field.get(delegate);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static int getPreferredNetworkType() {
        try {
            Object object = ReflectUtils.getITelephonyExt();
            Class c = object.getClass();
            Method method = c.getMethod("getPreferredNetworkType");
            return (int) method.invoke(object);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean setPreferredNetworkType(int mode, int registerNetwork) {
        try {
            Object object = ReflectUtils.getITelephonyExt();
            Class c = object.getClass();
            Method method = c.getMethod("setPreferredNetworkType", int.class, int.class);
            return (Boolean) method.invoke(object, mode, registerNetwork);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getPhoneSubID() {
        ///FIXME
        try {
            Object object = Utils.getITelephonyExt();
            Class c = object.getClass();
            Method method = c.getMethod("get34GCapabilityPhoneId");
            int slotId = (int) method.invoke(object);
            long[] subId = getSubId(slotId);
            if (subId == null || subId.length == 0) {
                return 0;
            }
            else {
                return (int) subId[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long[] getSubId(int slotId) {
        Method getSubId = getSubIdMethod();
        Object subIdObject = null;
        try {
            subIdObject = (Object) getSubId.invoke(null, new Object[]{
                    slotId
            });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // Flyme kuangqianbo add to adapt M81(Android5.1):
        // getSubId returned int[] {@
        long[] subId = null;
        if (subIdObject == null) {
            return subId;
        }

        try {
            // For Android 5.0
            subId = (long[]) subIdObject;
        } catch (ClassCastException e) {
            try {
                // For Android 5.1
                int[] intSubId = (int[])subIdObject;
                if (intSubId != null) {
                    subId = new long[intSubId.length];
                    for(int i = 0; i < intSubId.length; i++) {
                        subId[i] = (long)intSubId[i];
                    }
                }
            } catch (Exception normalException) {
                normalException.printStackTrace();
            }
        }
        // @}
        return subId;
    }

    private final static Method getSubIdMethod() {
        Method getSubIdMethod = null;
        getSubIdMethod = getMethod("android.telephony.SubscriptionManager", "getSubId", new Class[]{
                int.class
        });
        return getSubIdMethod;
    }

    public static Method getMethod(String pkgName, String methodName, Class<?>... parameterTypes) {
        Method method = null;
        try {
            method = getClassObject(pkgName).getDeclaredMethod(
                    methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;
    }

    public static Class<?> getClassObject(String pkgName) {
        Class<?> classObject = null;
        try {
            classObject = Class.forName(pkgName);
            return classObject;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return classObject;
    }
}
