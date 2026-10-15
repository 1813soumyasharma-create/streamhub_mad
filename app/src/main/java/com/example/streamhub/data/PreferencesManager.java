package com.example.streamhub.data;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {
    private final SharedPreferences prefs;

    public PreferencesManager(Context context) {
        this.prefs = context.getSharedPreferences("streamhub_prefs", Context.MODE_PRIVATE);
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean("is_logged_in", false);
    }

    public void setLoggedIn(boolean loggedIn) {
        prefs.edit().putBoolean("is_logged_in", loggedIn).apply();
    }

    public String getUserEmail() {
        return prefs.getString("user_email", "");
    }

    public void setUserEmail(String email) {
        prefs.edit().putString("user_email", email).apply();
    }

    public String getColorVisionMode() {
        return prefs.getString("color_vision_mode", "Deuteranopia");
    }

    public void setColorVisionMode(String mode) {
        prefs.edit().putString("color_vision_mode", mode).apply();
    }

    public boolean isAslEnabled() {
        return prefs.getBoolean("asl_enabled", true);
    }

    public void setAslEnabled(boolean enabled) {
        prefs.edit().putBoolean("asl_enabled", enabled).apply();
    }

    public boolean isAudioDescriptionEnabled() {
        return prefs.getBoolean("audio_desc_enabled", true);
    }

    public void setAudioDescriptionEnabled(boolean enabled) {
        prefs.edit().putBoolean("audio_desc_enabled", enabled).apply();
    }

    public boolean isHapticEnabled() {
        return prefs.getBoolean("haptic_enabled", true);
    }

    public void setHapticEnabled(boolean enabled) {
        prefs.edit().putBoolean("haptic_enabled", enabled).apply();
    }

    public boolean isCaptionsEnabled() {
        return prefs.getBoolean("captions_enabled", true);
    }

    public void setCaptionsEnabled(boolean enabled) {
        prefs.edit().putBoolean("captions_enabled", enabled).apply();
    }

    public float getTextScale() {
        return prefs.getFloat("text_scale", 1.0f);
    }

    public void setTextScale(float scale) {
        prefs.edit().putFloat("text_scale", scale).apply();
    }

    public float getSpeechRate() {
        return prefs.getFloat("speech_rate", 1.0f);
    }

    public void setSpeechRate(float rate) {
        prefs.edit().putFloat("speech_rate", rate).apply();
    }

    public boolean isNarrateEveryScreen() {
        return prefs.getBoolean("narrate_every_screen", true);
    }

    public void setNarrateEveryScreen(boolean narrate) {
        prefs.edit().putBoolean("narrate_every_screen", narrate).apply();
    }

    public boolean isCaptionBackground() {
        return prefs.getBoolean("caption_background", true);
    }

    public void setCaptionBackground(boolean bg) {
        prefs.edit().putBoolean("caption_background", bg).apply();
    }
}
