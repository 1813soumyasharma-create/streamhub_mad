package com.example.streamhub.data;

import com.example.streamhub.model.VideoItem;

import java.util.Arrays;
import java.util.List;

public class VideoRepository {
    public static List<VideoItem> getSampleVideos() {
        return Arrays.asList(
                new VideoItem("1", "Space Facts for Kids", "NASA Kids • 12K views", "4:00", "", "asset:///video_space.mp4", true, true, true, "Education", "2 days ago"),
                new VideoItem("2", "Learn ASL: Alphabet & Greetings", "SignLang Academy • 18K views", "8:15", "", "asset:///video_news.mp4", true, true, true, "Sign Lang", "Yesterday"),
                new VideoItem("3", "Accessible Piano Melodies", "SoundWave • 9K views", "5:30", "", "asset:///video_music.mp4", true, true, true, "Music", "3 days ago"),
                new VideoItem("4", "Global News Bulletin (ASL + CC)", "Global News • 35K views", "12:00", "", "asset:///video_news.mp4", true, true, true, "News", "Today"),
                new VideoItem("5", "Understanding Color Contrast in UI", "Design Access • 14K views", "6:45", "", "asset:///video_space.mp4", true, true, true, "Education", "4 days ago"),
                new VideoItem("6", "Sign Language Storytime: The Little Star", "StorySign • 22K views", "7:20", "", "asset:///video_news.mp4", true, true, true, "Sign Lang", "5 days ago"),
                new VideoItem("7", "Guitar Masterclass for Visually Impaired", "Acoustic Touch • 11K views", "10:10", "", "asset:///video_music.mp4", true, true, true, "Music", "1 week ago"),
                new VideoItem("8", "Tech Innovations for Accessibility", "Future Tech • 28K views", "9:50", "", "asset:///video_space.mp4", true, true, true, "Tech", "2 weeks ago"),
                new VideoItem("9", "Science Lab: Volcano Eruption Explained", "Junior Science • 16K views", "5:15", "", "asset:///video_space.mp4", true, true, true, "Education", "Yesterday"),
                new VideoItem("10", "ASL Cover: Top Pop Hits", "SignMelodies • 40K views", "3:45", "", "asset:///video_music.mp4", true, true, true, "Music", "Today")
        );
    }
}
