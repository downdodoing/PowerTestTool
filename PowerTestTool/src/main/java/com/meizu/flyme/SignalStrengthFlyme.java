package com.meizu.flyme;

public class SignalStrengthFlyme {

    public static final int NUM_SIGNAL_STRENGTH_BINS = ReflectUtils.getStaticVariableInt("android.telephony.SignalStrength", "NUM_SIGNAL_STRENGTH_BINS");
}
