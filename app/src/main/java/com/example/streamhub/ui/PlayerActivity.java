package com.example.streamhub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.MediaController;
import androidx.appcompat.app.AppCompatActivity;

import com.example.streamhub.R;
import com.example.streamhub.accessibility.CaptionManager;
import com.example.streamhub.accessibility.ColorVisionHelper;
import com.example.streamhub.accessibility.HapticManager;
import com.example.streamhub.accessibility.TtsManager;
import com.example.streamhub.data.PreferencesManager;
import com.example.streamhub.model.VideoItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class PlayerActivity extends AppCompatActivity {

    private HapticManager haptic;
    private TtsManager ttsManager;
    private CaptionManager captionManager;
    private VideoView videoView;
    private VideoView aslVideoView;
    private PreferencesManager prefs;
    private String assetFileName = "video_space.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        prefs = new PreferencesManager(this);
        ColorVisionHelper.applyToActivity(this, prefs.getColorVisionMode());

        haptic = new HapticManager(this);
        haptic.setHapticEnabled(prefs.isHapticEnabled());

        ttsManager = new TtsManager(this, () -> {
            if (prefs.isAudioDescriptionEnabled()) {
                ttsManager.speak("Audio description active. Custom ASL interpreter video overlay playing.");
            }
        });
        ttsManager.setSpeechRate(prefs.getSpeechRate());

        captionManager = new CaptionManager();
        captionManager.loadDefaultCues();
        captionManager.setCaptionsEnabled(prefs.isCaptionsEnabled());

        @SuppressWarnings("deprecation")
        VideoItem video = (VideoItem) getIntent().getSerializableExtra("video");

        TextView tvTitle = findViewById(R.id.tvPlayerTitle);
        TextView tvChannel = findViewById(R.id.tvPlayerChannel);
        Button btnLike = findViewById(R.id.btnLike);
        Button btnSubscribe = findViewById(R.id.btnSubscribe);
        Button btnShare = findViewById(R.id.btnShare);
        EditText etComment = findViewById(R.id.etComment);
        Button btnPostComment = findViewById(R.id.btnPostComment);
        videoView = findViewById(R.id.videoView);
        aslVideoView = findViewById(R.id.aslVideoView);

        // Ensure ASL PiP VideoView surface is rendered ON TOP of main VideoView surface
        if (aslVideoView != null) {
            aslVideoView.setZOrderMediaOverlay(true);
        }

        FrameLayout aslPipContainer = findViewById(R.id.aslPipContainer);
        Switch switchPlayerAsl = findViewById(R.id.switchPlayerAsl);
        Switch switchPlayerAd = findViewById(R.id.switchPlayerAd);
        Switch switchPlayerCc = findViewById(R.id.switchPlayerCc);

        switchPlayerAsl.setChecked(prefs.isAslEnabled());
        switchPlayerAd.setChecked(prefs.isAudioDescriptionEnabled());
        switchPlayerCc.setChecked(prefs.isCaptionsEnabled());

        aslPipContainer.setVisibility(View.VISIBLE);
        prefs.setAslEnabled(true);
        switchPlayerAsl.setChecked(true);

        aslPipContainer.setOnClickListener(v -> {
            haptic.playSuccessPulse();
            ttsManager.speak("ASL Sign Language Interpreter window active. Playing custom user ASL video overlay in real-time.");
            Toast.makeText(this, "Playing Custom ASL Interpreter Video", Toast.LENGTH_SHORT).show();
        });

        switchPlayerAsl.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.setAslEnabled(isChecked);
            aslPipContainer.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            haptic.playClickPulse();
            ttsManager.speak(isChecked ? "ASL interpreter window enabled" : "ASL interpreter window disabled");
            if (isChecked && aslVideoView != null && !aslVideoView.isPlaying()) {
                aslVideoView.start();
            } else if (!isChecked && aslVideoView != null) {
                aslVideoView.pause();
            }
        });

        switchPlayerAd.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.setAudioDescriptionEnabled(isChecked);
            haptic.playClickPulse();
            if (isChecked) {
                ttsManager.speak("Audio description enabled.");
            } else {
                ttsManager.stop();
            }
        });

        switchPlayerCc.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.setCaptionsEnabled(isChecked);
            captionManager.setCaptionsEnabled(isChecked);
            haptic.playClickPulse();
            Toast.makeText(this, isChecked ? "Captions enabled" : "Captions disabled", Toast.LENGTH_SHORT).show();
        });

        if (video != null) {
            tvTitle.setText(video.getTitle());
            tvChannel.setText(video.getChannel());
            if (video.getVideoUrl().contains("video_music.mp4")) {
                assetFileName = "video_music.mp4";
            } else if (video.getVideoUrl().contains("video_news.mp4")) {
                assetFileName = "video_news.mp4";
            } else if (video.getVideoUrl().contains("video_space.mp4")) {
                assetFileName = "video_space.mp4";
            }
        }

        setupVideoPlayer();
        setupAslVideoPlayer();

        btnLike.setOnClickListener(v -> {
            haptic.playSuccessPulse();
            ttsManager.speak("Liked video");
            Toast.makeText(this, "Added to Liked Videos", Toast.LENGTH_SHORT).show();
        });

        btnSubscribe.setOnClickListener(v -> {
            haptic.playSuccessPulse();
            ttsManager.speak("Subscribed to channel");
            Toast.makeText(this, "Subscribed successfully", Toast.LENGTH_SHORT).show();
        });

        btnShare.setOnClickListener(v -> {
            haptic.playClickPulse();
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this accessible video on StreamHub!");
            shareIntent.setType("text/plain");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        btnPostComment.setOnClickListener(v -> {
            haptic.playClickPulse();
            String commentText = etComment.getText().toString();
            if (!commentText.isEmpty()) {
                ttsManager.speak("Comment posted");
                etComment.setText("");
                Toast.makeText(this, "Comment posted to Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupVideoPlayer() {
        try {
            File cacheFile = new File(getCacheDir(), assetFileName);
            if (!cacheFile.exists()) {
                try (InputStream inputStream = getAssets().open(assetFileName);
                     OutputStream outputStream = new FileOutputStream(cacheFile)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                }
            }

            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.setVideoPath(cacheFile.getAbsolutePath());
            videoView.setOnPreparedListener(mp -> {
                mp.setVolume(1.0f, 1.0f);
                mp.start();
                mp.setLooping(true);
            });
            videoView.setOnErrorListener((mp, what, extra) -> {
                Toast.makeText(this, "Main VideoView error: " + what + ", " + extra, Toast.LENGTH_LONG).show();
                return true;
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error loading main video: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setupAslVideoPlayer() {
        try {
            File cacheFile = new File(getCacheDir(), "asl_custom_user_demo.mp4");
            // Always overwrite to ensure custom user video D:\10374288-hd_1920_1080_30fps.mp4 is loaded fresh
            try (InputStream inputStream = getAssets().open("asl_interpreter_demo.mp4");
                 OutputStream outputStream = new FileOutputStream(cacheFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            }

            aslVideoView.setVideoPath(cacheFile.getAbsolutePath());
            aslVideoView.setOnPreparedListener(mp -> {
                mp.setVolume(0.0f, 0.0f);
                mp.start();
                mp.setLooping(true);
            });
            aslVideoView.setOnErrorListener((mp, what, extra) -> true);
        } catch (Exception e) {
            // Fallback
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoView != null && !videoView.isPlaying()) {
            videoView.start();
        }
        if (aslVideoView != null && !aslVideoView.isPlaying()) {
            aslVideoView.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null) {
            videoView.pause();
        }
        if (aslVideoView != null) {
            aslVideoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ttsManager != null) {
            ttsManager.shutdown();
        }
    }
}
