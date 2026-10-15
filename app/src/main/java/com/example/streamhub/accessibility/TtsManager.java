package com.example.streamhub.accessibility;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class TtsManager implements TextToSpeech.OnInitListener {

    public interface OnInitializedListener {
        void onInitialized();
    }

    private TextToSpeech tts;
    private final OnInitializedListener onInitialized;
    private boolean isTtsEnabled = true;
    private float speechRate = 1.0f;

    public TtsManager(Context context, OnInitializedListener onInitialized) {
        this.onInitialized = onInitialized;
        this.tts = new TextToSpeech(context, this);
    }

    public TtsManager(Context context) {
        this(context, null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS && tts != null) {
            tts.setLanguage(Locale.US);
            tts.setSpeechRate(speechRate);
            if (onInitialized != null) {
                onInitialized.onInitialized();
            }
        }
    }

    public boolean isTtsEnabled() {
        return isTtsEnabled;
    }

    public void setTtsEnabled(boolean ttsEnabled) {
        isTtsEnabled = ttsEnabled;
    }

    public float getSpeechRate() {
        return speechRate;
    }

    public void setSpeechRate(float speechRate) {
        this.speechRate = speechRate;
        if (tts != null) {
            tts.setSpeechRate(speechRate);
        }
    }

    public void speak(String text, int queueMode) {
        if (!isTtsEnabled || tts == null) return;
        tts.speak(text, queueMode, null, null);
    }

    public void speak(String text) {
        speak(text, TextToSpeech.QUEUE_FLUSH);
    }

    public void stop() {
        if (tts != null) {
            tts.stop();
        }
    }

    public void shutdown() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }
}
