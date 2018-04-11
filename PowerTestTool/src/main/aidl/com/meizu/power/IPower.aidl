// IPower.aidl
package com.meizu.power;

// Declare any non-default types here with import statements
import com.meizu.power.powergauge.RemoteDetaTimeStats;
import com.meizu.power.powergauge.RemoteDetaPowerSw;

interface IPower {
    //IPowerApp
    void cleanApps(boolean cleanAll, boolean ifWithNotification);

    int getInstalledWhiteAppNum();

    //IPowerMode
    void enterPowerMode(int mode);
    void outPowerMode();
    int getCurrentMode();

    //IPowerStats
    int calculateStandbyMinutes();

    // should be called first
    int calculateUseMinutes(int powerMode);

    // should be called after calculateUseMinutes
    int calculateTimePercent();

    // should be called after calculateUseMinutes
    int calculatePowerChangeTime();

    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    //IPowerGauge
    RemoteDetaTimeStats getRemoteDetaTimeStats();
    List<RemoteDetaPowerSw> getRemoteDetaPowerSw();
}
