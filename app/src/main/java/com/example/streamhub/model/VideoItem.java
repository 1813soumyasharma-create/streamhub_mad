package com.example.streamhub.model;

import java.io.Serializable;

public class VideoItem implements Serializable {
    private final String id;
    private final String title;
    private final String channel;
    private final String duration;
    private final String thumbnailUrl;
    private final String videoUrl;
    private final boolean hasAsl;
    private final boolean hasCc;
    private final boolean hasAd;
    private final String category;
    private final String timestamp;

    public VideoItem(String id, String title, String channel, String duration, String thumbnailUrl, String videoUrl, boolean hasAsl, boolean hasCc, boolean hasAd, String category, String timestamp) {
        this.id = id;
        this.title = title;
        this.channel = channel;
        this.duration = duration;
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
        this.hasAsl = hasAsl;
        this.hasCc = hasCc;
        this.hasAd = hasAd;
        this.category = category;
        this.timestamp = timestamp;
    }

    public VideoItem(String id, String title, String channel, String duration, String thumbnailUrl, String videoUrl) {
        this(id, title, channel, duration, thumbnailUrl, videoUrl, true, true, true, "Education", "2 days ago");
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getChannel() { return channel; }
    public String getDuration() { return duration; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public String getVideoUrl() { return videoUrl; }
    public boolean isHasAsl() { return hasAsl; }
    public boolean isHasCc() { return hasCc; }
    public boolean isHasAd() { return hasAd; }
    public String getCategory() { return category; }
    public String getTimestamp() { return timestamp; }
}
