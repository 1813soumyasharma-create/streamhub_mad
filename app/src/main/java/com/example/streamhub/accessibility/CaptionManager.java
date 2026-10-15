package com.example.streamhub.accessibility;

import java.util.ArrayList;
import java.util.List;

public class CaptionManager {

    public static class CaptionCue {
        private final long startTimeMs;
        private final long endTimeMs;
        private final String text;

        public CaptionCue(long startTimeMs, long endTimeMs, String text) {
            this.startTimeMs = startTimeMs;
            this.endTimeMs = endTimeMs;
            this.text = text;
        }

        public long getStartTimeMs() {
            return startTimeMs;
        }

        public long getEndTimeMs() {
            return endTimeMs;
        }

        public String getText() {
            return text;
        }
    }

    private final List<CaptionCue> cues = new ArrayList<>();
    private boolean isCaptionsEnabled = true;
    private boolean hasCaptionBackground = true;

    public void loadDefaultCues() {
        cues.clear();
        cues.add(new CaptionCue(0, 4000, "Welcome to StreamHub! Streaming for everyone."));
        cues.add(new CaptionCue(4001, 8000, "[ASL Interpreter active: Greeting viewers in sign language]"));
        cues.add(new CaptionCue(8001, 12000, "Audio description: A rocket launches from a sandy pad into a bright blue sky."));
        cues.add(new CaptionCue(12001, 20000, "Explore accessible features: ASL, Closed Captions, Audio Descriptions, Haptics."));
    }

    public String getCaptionForTime(long currentPositionMs) {
        if (!isCaptionsEnabled) return "";
        for (CaptionCue cue : cues) {
            if (currentPositionMs >= cue.getStartTimeMs() && currentPositionMs <= cue.getEndTimeMs()) {
                return cue.getText();
            }
        }
        return "";
    }

    public boolean isCaptionsEnabled() {
        return isCaptionsEnabled;
    }

    public void setCaptionsEnabled(boolean captionsEnabled) {
        isCaptionsEnabled = captionsEnabled;
    }

    public boolean isHasCaptionBackground() {
        return hasCaptionBackground;
    }

    public void setHasCaptionBackground(boolean hasCaptionBackground) {
        this.hasCaptionBackground = hasCaptionBackground;
    }
}
