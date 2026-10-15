package com.example.streamhub.ui;

import android.os.Bundle;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.streamhub.accessibility.HapticManager;
import com.example.streamhub.accessibility.TtsManager;

public class HelpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webView = new WebView(this);
        setContentView(webView);
        webView.loadData("<html><body style='background:#0F172A;color:white;padding:24dp;'><h2>StreamHub Help & Guide</h2><p>StreamHub is designed for universal accessibility, featuring ASL translation windows, closed captions, audio descriptions, haptic pulses, color blindness correction, SQLite history and CSV export.</p></body></html>", "text/html", "UTF-8");

        HapticManager haptic = new HapticManager(this);
        TtsManager ttsManager = new TtsManager(this, () -> {
            // initialized
        });
        ttsManager.speak("Help and guide loaded.");
        haptic.playClickPulse();
    }
}
