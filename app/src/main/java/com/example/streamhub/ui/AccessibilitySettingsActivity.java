package com.example.streamhub.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

import com.example.streamhub.R;
import com.example.streamhub.accessibility.ColorVisionHelper;
import com.example.streamhub.accessibility.HapticManager;
import com.example.streamhub.accessibility.TtsManager;
import com.example.streamhub.data.PreferencesManager;

public class AccessibilitySettingsActivity extends AppCompatActivity {

    private TtsManager ttsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessibility_settings);

        PreferencesManager prefs = new PreferencesManager(this);
        HapticManager haptic = new HapticManager(this);
        ttsManager = new TtsManager(this, () -> ttsManager.setSpeechRate(prefs.getSpeechRate()));

        ColorVisionHelper.applyToActivity(this, prefs.getColorVisionMode());

        Spinner spinnerColorMode = findViewById(R.id.spinnerColorMode);
        SeekBar seekSpeech = findViewById(R.id.seekSpeech);
        Switch switchNarrate = findViewById(R.id.switchNarrate);
        Switch switchHapticPattern = findViewById(R.id.switchHapticPattern);
        Switch switchCaptionBg = findViewById(R.id.switchCaptionBg);
        Button btnTestHaptics = findViewById(R.id.btnTestHaptics);

        switchNarrate.setChecked(prefs.isNarrateEveryScreen());
        switchHapticPattern.setChecked(prefs.isHapticEnabled());
        switchCaptionBg.setChecked(prefs.isCaptionBackground());
        seekSpeech.setProgress(Math.min(100, Math.max(0, (int) ((prefs.getSpeechRate() - 0.5f) * 100))));

        String[] modes = {"Normal", "Protanopia (red-weak)", "Deuteranopia (green-weak)", "Tritanopia (blue-weak)", "High contrast"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, modes);
        spinnerColorMode.setAdapter(adapter);

        String currentMode = prefs.getColorVisionMode();
        int selectedIndex = -1;
        for (int i = 0; i < modes.length; i++) {
            if (modes[i].toLowerCase().contains(currentMode.toLowerCase())) {
                selectedIndex = i;
                break;
            }
        }
        if (selectedIndex >= 0) {
            spinnerColorMode.setSelection(selectedIndex);
        }

        spinnerColorMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private boolean isInitial = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isInitial) {
                    isInitial = false;
                    return;
                }
                String selectedMode = modes[position];
                String shortMode;
                String lower = selectedMode.toLowerCase();
                if (lower.contains("protanopia")) shortMode = "Protanopia";
                else if (lower.contains("deuteranopia")) shortMode = "Deuteranopia";
                else if (lower.contains("tritanopia")) shortMode = "Tritanopia";
                else if (lower.contains("high contrast")) shortMode = "High contrast";
                else shortMode = "Normal";

                prefs.setColorVisionMode(shortMode);
                ColorVisionHelper.applyToActivity(AccessibilitySettingsActivity.this, shortMode);
                haptic.playClickPulse();
                ttsManager.speak("Color vision mode updated to " + shortMode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        switchNarrate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.setNarrateEveryScreen(isChecked);
            haptic.playClickPulse();
            ttsManager.speak(isChecked ? "Screen narration enabled" : "Screen narration disabled");
        });

        switchHapticPattern.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.setHapticEnabled(isChecked);
            haptic.setHapticEnabled(isChecked);
            if (isChecked) haptic.playSuccessPulse();
        });

        switchCaptionBg.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.setCaptionBackground(isChecked);
            haptic.playClickPulse();
        });

        seekSpeech.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    float rate = 0.5f + (progress / 100f) * 1.5f;
                    prefs.setSpeechRate(rate);
                    ttsManager.setSpeechRate(rate);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                haptic.playClickPulse();
                ttsManager.speak("Speech rate adjusted");
            }
        });

        btnTestHaptics.setOnClickListener(v -> {
            haptic.playSuccessPulse();
            ttsManager.speak("Testing haptics and voice output. Accessibility engine functioning correctly.");
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
