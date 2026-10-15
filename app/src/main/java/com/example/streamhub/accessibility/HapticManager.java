package com.example.streamhub.accessibility;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;

public class HapticManager {

    private final Vibrator vibrator;
    private boolean isHapticEnabled = true;

    public HapticManager(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibratorManager vibratorManager = (VibratorManager) context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
            vibrator = vibratorManager != null ? vibratorManager.getDefaultVibrator() : null;
        } else {
            @SuppressWarnings("deprecation")
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator = v;
        }
    }

    public boolean isHapticEnabled() {
        return isHapticEnabled;
    }

    public void setHapticEnabled(boolean hapticEnabled) {
        isHapticEnabled = hapticEnabled;
    }

    public void playClickPulse() {
        if (!isHapticEnabled || vibrator == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));
        } else {
            @SuppressWarnings("deprecation")
            long vTime = 50;
            vibrator.vibrate(vTime);
        }
    }

    public void playSuccessPulse() {
        if (!isHapticEnabled || vibrator == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK));
        } else {
            @SuppressWarnings("deprecation")
            long[] timings = new long[]{0, 70, 50, 70};
            vibrator.vibrate(timings, -1);
        }
    }

    public void playAlertPulse() {
        if (!isHapticEnabled || vibrator == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(new long[]{0, 150, 100, 150}, new int[]{0, 255, 0, 255}, -1));
        } else {
            @SuppressWarnings("deprecation")
            long vTime = 300;
            vibrator.vibrate(vTime);
        }
    }
}
