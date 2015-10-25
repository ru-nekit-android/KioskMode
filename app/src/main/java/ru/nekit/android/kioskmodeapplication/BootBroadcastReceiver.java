package ru.nekit.android.kioskmodeapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class BootBroadcastReceiver extends WakefulBroadcastReceiver {
    public BootBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppContext.startPeriodicAlarm(context);
        WakefulBroadcastReceiver.completeWakefulIntent(intent);
    }
}
