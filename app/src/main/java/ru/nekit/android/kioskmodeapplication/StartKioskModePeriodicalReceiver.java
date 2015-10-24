package ru.nekit.android.kioskmodeapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StartKioskModePeriodicalReceiver extends BroadcastReceiver {

    public static final int REQUEST_CODE = 1;

    public StartKioskModePeriodicalReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppContext.startKioskActivity(context, KioskActivity.class);
        Log.v("ru.nekit.android.vtag", "Call startKioskActivity");
    }
}
