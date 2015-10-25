package ru.nekit.android.kioskmodeapplication;

import android.annotation.TargetApi;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.nekit.android.kioskmodeapplication.databinding.ActivityKioskBinding;

public class KioskActivity extends AppCompatActivity implements View.OnClickListener {

    private static final List BLOCKED_KEYS = new ArrayList(Arrays.asList(KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_UP, KeyEvent.KEYCODE_HOME));
    private HomeKeyLocker mHomeKeyLocker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        makeFullScreen();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        ActivityKioskBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_kiosk);
        binding.exitButton.setOnClickListener(this);
        binding.destroyButton.setOnClickListener(this);
        ActionBar toolbar = getSupportActionBar();
        assert toolbar != null;
        toolbar.setDisplayHomeAsUpEnabled(false);
        getAppContext().registerKioskModeScreenOffReceiver();
        mHomeKeyLocker = new HomeKeyLocker();
        mHomeKeyLocker.lock(this);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void makeFullScreen() {
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT < 19) { //View.SYSTEM_UI_FLAG_IMMERSIVE is only on API 19+
            window.getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } else {
            window.getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    private AppContext getAppContext() {
        return (AppContext) getApplicationContext();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.exit_button:

                finish();

                break;

            case R.id.destroy_button:

                AppContext.cancelPeriodicAlarm(getAppContext());
                finish();

                break;

            default:
                break;

        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }

    @Override
    protected void onDestroy() {
        getAppContext().unregisterKioskModeScreenOffReceiver();
        mHomeKeyLocker.unlock();
        mHomeKeyLocker = null;
        super.onDestroy();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return BLOCKED_KEYS.contains(event.getKeyCode()) || super.dispatchKeyEvent(event);
    }
}


