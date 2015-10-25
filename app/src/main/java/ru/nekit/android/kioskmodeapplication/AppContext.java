package ru.nekit.android.kioskmodeapplication;

import android.app.AlarmManager;
import android.app.Application;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import android.os.SystemClock;
import android.view.WindowManager;

import java.util.concurrent.TimeUnit;

/**
 * Created by MacOS on 20.10.15.
 */
public class AppContext extends Application {

    private ScreenOffReceiver screenOffReceiver;
    private PowerManager.WakeLock mWakeLock;
    private KeyguardManager.KeyguardLock mKeyLock;

    public static void startPeriodicAlarm(Context context) {
        Intent intent = new Intent(context, StartKioskModePeriodicalReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, StartKioskModePeriodicalReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMillis = System.currentTimeMillis();
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                TimeUnit.MINUTES.toMillis(1), pIntent);
    }

    public static void cancelPeriodicAlarm(Context context) {
        Intent intent = new Intent(context, StartKioskModePeriodicalReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, StartKioskModePeriodicalReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }

    public static void startKioskActivity(Context context, Class kioskActivityClass) {
        ComponentName cn = new ComponentName(context.getPackageName(), kioskActivityClass.getName());
        context.getPackageManager().setComponentEnabledSetting(cn, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        Intent intent = new Intent(context, kioskActivityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
        context.startActivity(intent);
    }

    public void registerKioskModeScreenOffReceiver() {
        if (screenOffReceiver == null) {
            screenOffReceiver = new ScreenOffReceiver();
            registerReceiver(screenOffReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        }
    }

    public void unregisterKioskModeScreenOffReceiver() {
        if (screenOffReceiver != null) {
            unregisterReceiver(screenOffReceiver);
            screenOffReceiver = null;
        }
    }

    public PowerManager.WakeLock getWakeLock() {
        if (mWakeLock == null) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.ON_AFTER_RELEASE, "wakeup");
        }
        return mWakeLock;
    }

    public KeyguardManager.KeyguardLock getKeyguardLock() {
        if (mKeyLock == null) {
            KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            mKeyLock = km.newKeyguardLock("keyguardLock");
        }
        return mKeyLock;
    }

}
