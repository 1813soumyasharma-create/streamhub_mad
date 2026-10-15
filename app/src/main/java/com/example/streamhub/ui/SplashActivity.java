package com.example.streamhub.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

import com.example.streamhub.R;
import com.example.streamhub.accessibility.ColorVisionHelper;
import com.example.streamhub.accessibility.HapticManager;
import com.example.streamhub.accessibility.TtsManager;
import com.example.streamhub.data.PreferencesManager;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        PreferencesManager prefs = new PreferencesManager(this);
        ColorVisionHelper.applyToActivity(this, prefs.getColorVisionMode());

        HapticManager haptic = new HapticManager(this);
        haptic.playClickPulse();

        TtsManager ttsManager = new TtsManager(this, () -> {
            // initialized
        });
        ttsManager.speak("Welcome to StreamHub. Streaming for everyone.");

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent;
            if (prefs.isLoggedIn()) {
                intent = new Intent(this, MainActivity.class);
            } else {
                intent = new Intent(this, LoginRegisterActivity.class);
            }
            startActivity(intent);
            finish();
        }, 2000);
    }
}
