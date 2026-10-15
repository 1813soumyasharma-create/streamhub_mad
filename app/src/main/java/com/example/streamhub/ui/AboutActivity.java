package com.example.streamhub.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.streamhub.accessibility.HapticManager;
import com.example.streamhub.accessibility.TtsManager;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 48, 48, 48);
        layout.setBackgroundColor(Color.parseColor("#0F172A"));

        TextView tvTitle = new TextView(this);
        tvTitle.setText("StreamHub v1.0");
        tvTitle.setTextSize(24f);
        tvTitle.setTextColor(Color.WHITE);

        TextView tvDesc = new TextView(this);
        tvDesc.setText("Streaming for everyone. Universal accessibility engine built with Android Material 3, Media3 ExoPlayer, SQLite, and Google Maps.");
        tvDesc.setTextSize(14f);
        tvDesc.setTextColor(Color.parseColor("#94A3B8"));
        tvDesc.setPadding(0, 24, 0, 0);

        layout.addView(tvTitle);
        layout.addView(tvDesc);
        setContentView(layout);

        HapticManager haptic = new HapticManager(this);
        TtsManager ttsManager = new TtsManager(this, () -> {
            // initialized
        });
        ttsManager.speak("About StreamHub version 1.0.");
        haptic.playClickPulse();
    }
}
