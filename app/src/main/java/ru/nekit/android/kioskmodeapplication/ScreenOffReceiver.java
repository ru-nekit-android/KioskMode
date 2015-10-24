package ru.nekit.android.kioskmodeapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

/**
 * Created by MacOS on 20.10.15.
 */
public class ScreenOffReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            wakeUpDevice((AppContext) context.getApplicationContext());
        }
    }

    private void wakeUpDevice(AppContext appContext) {
        appContext.getKeyguardLock().disableKeyguard();
        PowerManager.WakeLock mWakeLock = appContext.getWakeLock();
        if (mWakeLock.isHeld()) {
            mWakeLock.release();
        }
        mWakeLock.acquire(0);
        mWakeLock.release();
    }
}
