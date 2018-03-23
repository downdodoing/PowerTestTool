package com.meizu.flyme;

import java.lang.reflect.Method;

public class VoiceSenseServiceFlyme {

    public static void voiceSenseEnable(boolean enable, String flag) {
        String CVQ_ACTIVATE_KEYWORDS = "CVQ Activate Keywords";
        String AUDIENCE_CVQ_ACTIVATE_KWS_PROPERTY = "persist.sys.audience.selkw";
        try {
            Object object = ReflectUtils.getIVoiceSenseService();
            Class c = object.getClass();
            Method method = c.getMethod("setValue", String.class, String.class);
            method.invoke(object, CVQ_ACTIVATE_KEYWORDS, flag);
            SystemPropertiesFlyme.set(AUDIENCE_CVQ_ACTIVATE_KWS_PROPERTY, flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
