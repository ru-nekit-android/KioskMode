package ru.nekit.android.kioskmodeapplication;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import ru.nekit.android.kioskmodeapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.kioskModeButton.setOnClickListener(this);
        AppContext.startPeriodicAlarm(getApplicationContext());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.kiosk_mode_button:

                AppContext.startKioskActivity(getApplicationContext(), KioskActivity.class);

                break;

            default:
                break;
        }
    }
}
