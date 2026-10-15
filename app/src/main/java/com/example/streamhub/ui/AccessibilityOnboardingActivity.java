package com.example.streamhub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

import com.example.streamhub.R;
import com.example.streamhub.accessibility.ColorVisionHelper;
import com.example.streamhub.accessibility.HapticManager;
import com.example.streamhub.accessibility.TtsManager;
import com.example.streamhub.data.PreferencesManager;

public class AccessibilityOnboardingActivity extends AppCompatActivity {

    private TtsManager ttsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessibility_onboarding);

        PreferencesManager prefs = new PreferencesManager(this);
        HapticManager haptic = new HapticManager(this);
        ttsManager = new TtsManager(this, () -> ttsManager.speak("Make StreamHub yours. Select your color vision mode and accessibility features."));

        ColorVisionHelper.applyToActivity(this, prefs.getColorVisionMode());

        RadioGroup rgColorVision = findViewById(R.id.rgColorVision);
        Switch switchAsl = findViewById(R.id.switchAsl);
        Switch switchAd = findViewById(R.id.switchAd);
        Switch switchHaptic = findViewById(R.id.switchHaptic);
        Switch switchCaptions = findViewById(R.id.switchCaptions);
        SeekBar seekBarTextSize = findViewById(R.id.seekBarTextSize);
        Button btnContinue = findViewById(R.id.btnContinue);

        String currentMode = prefs.getColorVisionMode();
        if (currentMode.toLowerCase().contains("normal")) {
            rgColorVision.check(R.id.rbNormal);
        } else if (currentMode.toLowerCase().contains("protanopia")) {
            rgColorVision.check(R.id.rbProtanopia);
        } else if (currentMode.toLowerCase().contains("deuteranopia")) {
            rgColorVision.check(R.id.rbDeuteranopia);
        } else if (currentMode.toLowerCase().contains("tritanopia")) {
            rgColorVision.check(R.id.rbTritanopia);
        } else if (currentMode.toLowerCase().contains("high contrast")) {
            rgColorVision.check(R.id.rbHighContrast);
        } else {
            rgColorVision.check(R.id.rbDeuteranopia);
        }

        switchAsl.setChecked(prefs.isAslEnabled());
        switchAd.setChecked(prefs.isAudioDescriptionEnabled());
        switchHaptic.setChecked(prefs.isHapticEnabled());
        switchCaptions.setChecked(prefs.isCaptionsEnabled());
        seekBarTextSize.setProgress((int) ((prefs.getTextScale() - 0.8f) * 200));

        rgColorVision.setOnCheckedChangeListener((group, checkedId) -> {
            haptic.playClickPulse();
            String modeName;
            if (checkedId == R.id.rbNormal) modeName = "Normal";
            else if (checkedId == R.id.rbProtanopia) modeName = "Protanopia";
            else if (checkedId == R.id.rbDeuteranopia) modeName = "Deuteranopia";
            else if (checkedId == R.id.rbTritanopia) modeName = "Tritanopia";
            else if (checkedId == R.id.rbHighContrast) modeName = "High contrast";
            else modeName = "Deuteranopia";

            prefs.setColorVisionMode(modeName);
            ColorVisionHelper.applyToActivity(this, modeName);
            ttsManager.speak("Color vision mode set to " + modeName);
        });

        switchAsl.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.setAslEnabled(isChecked);
            haptic.playClickPulse();
        });

        switchAd.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.setAudioDescriptionEnabled(isChecked);
            haptic.playClickPulse();
        });

        switchHaptic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.setHapticEnabled(isChecked);
            haptic.setHapticEnabled(isChecked);
            if (isChecked) haptic.playSuccessPulse();
        });

        switchCaptions.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.setCaptionsEnabled(isChecked);
            haptic.playClickPulse();
        });

        btnContinue.setOnClickListener(v -> {
            haptic.playSuccessPulse();
            ttsManager.speak("Accessibility preferences saved. Entering StreamHub.");

            prefs.setAslEnabled(switchAsl.isChecked());
            prefs.setAudioDescriptionEnabled(switchAd.isChecked());
            prefs.setHapticEnabled(switchHaptic.isChecked());
            prefs.setCaptionsEnabled(switchCaptions.isChecked());
            prefs.setTextScale(0.8f + (seekBarTextSize.getProgress() / 200f));

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ttsManager != null) {
            ttsManager.shutdown();
        }
    }
}
