
package com.meizu.flyme;

import java.lang.reflect.Field;

public class FeatureFlyme {
    public static boolean TELECOM_NFC_FEATURE;
    public static boolean SHELL_SMART_VOICE;
    static {

        try {
            Class<?> delegate = Class.forName("flyme.config.FlymeFeature");
            Field field = delegate.getDeclaredField("TELECOM_NFC_FEATURE");
            field.setAccessible(true);
            TELECOM_NFC_FEATURE = (Boolean) field.get(delegate);
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
            Class<?> delegate = Class.forName("flyme.config.FlymeFeature");
            Field field = delegate.getDeclaredField("SHELL_SMART_VOICE");
            field.setAccessible(true);
            SHELL_SMART_VOICE = (Boolean) field.get(delegate);
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

}
