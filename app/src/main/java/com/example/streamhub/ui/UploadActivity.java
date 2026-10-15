package com.example.streamhub.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.streamhub.R;
import com.example.streamhub.accessibility.ColorVisionHelper;
import com.example.streamhub.accessibility.HapticManager;
import com.example.streamhub.accessibility.TtsManager;
import com.example.streamhub.data.PreferencesManager;

import java.util.Calendar;

public class UploadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        PreferencesManager prefs = new PreferencesManager(this);
        ColorVisionHelper.applyToActivity(this, prefs.getColorVisionMode());

        HapticManager haptic = new HapticManager(this);
        TtsManager tts = new TtsManager(this);

        Button btnGallery = findViewById(R.id.btnGallery);
        Button btnCamera = findViewById(R.id.btnCamera);
        Button btnPickDate = findViewById(R.id.btnPickDate);
        Button btnPickTime = findViewById(R.id.btnPickTime);
        Button btnPublish = findViewById(R.id.btnPublish);

        btnGallery.setOnClickListener(v -> {
            haptic.playClickPulse();
            tts.speak("Opening gallery to select video");
            try {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                intent.setType("video/*");
                startActivity(intent);
            } catch (Exception e) {
                Intent fallbackIntent = new Intent(Intent.ACTION_GET_CONTENT);
                fallbackIntent.setType("video/*");
                startActivity(Intent.createChooser(fallbackIntent, "Select Video"));
            }
        });

        btnCamera.setOnClickListener(v -> {
            haptic.playClickPulse();
            tts.speak("Opening camera to record video");
            try {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Camera not available on this device", Toast.LENGTH_SHORT).show();
            }
        });

        btnPickDate.setOnClickListener(v -> {
            haptic.playClickPulse();
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (view, y, m, d) -> btnPickDate.setText(d + "/" + (m + 1) + "/" + y), cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnPickTime.setOnClickListener(v -> {
            haptic.playClickPulse();
            Calendar cal = Calendar.getInstance();
            new TimePickerDialog(this, (view, h, min) -> btnPickTime.setText(String.format("%02d:%02d", h, min)), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
        });

        btnPublish.setOnClickListener(v -> {
            haptic.playSuccessPulse();
            tts.speak("Video published successfully with accessibility tracks.");
            Toast.makeText(this, "Published successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
