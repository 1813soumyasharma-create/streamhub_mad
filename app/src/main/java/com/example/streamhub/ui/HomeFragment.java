package com.example.streamhub.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.streamhub.R;
import com.example.streamhub.accessibility.HapticManager;
import com.example.streamhub.accessibility.TtsManager;
import com.example.streamhub.data.VideoRepository;
import com.example.streamhub.model.VideoItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private HapticManager haptic;
    private TtsManager tts;
    private RecyclerView rvVideos;
    private Button btnCatAll, btnCatEducation, btnCatMusic, btnCatSignLang;

    private final List<VideoItem> allVideos = VideoRepository.getSampleVideos();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        haptic = new HapticManager(requireContext());
        tts = new TtsManager(requireContext());

        Button btnUpload = view.findViewById(R.id.btnUpload);
        Button btnSearch = view.findViewById(R.id.btnSearch);
        btnCatAll = view.findViewById(R.id.btnCatAll);
        btnCatEducation = view.findViewById(R.id.btnCatEducation);
        btnCatMusic = view.findViewById(R.id.btnCatMusic);
        btnCatSignLang = view.findViewById(R.id.btnCatSignLang);

        rvVideos = view.findViewById(R.id.rvVideos);
        rvVideos.setLayoutManager(new LinearLayoutManager(requireContext()));

        btnUpload.setOnClickListener(v -> {
            haptic.playClickPulse();
            tts.speak("Opening Video Upload");
            startActivity(new Intent(requireContext(), UploadActivity.class));
        });

        btnSearch.setOnClickListener(v -> {
            haptic.playClickPulse();
            tts.speak("Opening Search");
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, new ExploreFragment())
                        .commit();
            }
        });

        setupCategoryButtons();
        filterVideos("All");

        return view;
    }

    private void setupCategoryButtons() {
        List<Button> buttons = Arrays.asList(btnCatAll, btnCatEducation, btnCatMusic, btnCatSignLang);
        List<String> categories = Arrays.asList("All", "Education", "Music", "Sign Lang");

        for (int i = 0; i < buttons.size(); i++) {
            final int index = i;
            Button button = buttons.get(i);
            button.setOnClickListener(v -> {
                haptic.playClickPulse();
                String cat = categories.get(index);
                tts.speak("Filtering by " + cat);
                highlightButton(button, buttons);
                filterVideos(cat);
            });
        }
    }

    private void highlightButton(Button selected, List<Button> allButtons) {
        for (Button btn : allButtons) {
            if (btn == selected) {
                btn.setBackgroundColor(Color.parseColor("#2563EB"));
                btn.setTextColor(Color.WHITE);
            } else {
                btn.setBackgroundColor(Color.parseColor("#1E293B"));
                btn.setTextColor(Color.parseColor("#94A3B8"));
            }
        }
    }

    private void filterVideos(String category) {
        List<VideoItem> filtered = new ArrayList<>();
        if ("All".equals(category)) {
            filtered = allVideos;
        } else {
            for (VideoItem item : allVideos) {
                if (item.getCategory().equalsIgnoreCase(category)) {
                    filtered.add(item);
                }
            }
        }
        rvVideos.setAdapter(new VideoAdapter(filtered, video -> {
            haptic.playClickPulse();
            tts.speak("Playing " + video.getTitle());
            Intent intent = new Intent(requireContext(), PlayerActivity.class);
            intent.putExtra("video", video);
            startActivity(intent);
        }));
    }

    public interface OnVideoClickListener {
        void onVideoClick(VideoItem video);
    }

    public static class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

        private final List<VideoItem> videos;
        private final OnVideoClickListener onClick;

        public VideoAdapter(List<VideoItem> videos, OnVideoClickListener onClick) {
            this.videos = videos;
            this.onClick = onClick;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final TextView tvTitle;
            public final TextView tvChannel;
            public final TextView tvDuration;

            public ViewHolder(View view) {
                super(view);
                tvTitle = view.findViewById(R.id.tvTitle);
                tvChannel = view.findViewById(R.id.tvChannel);
                tvDuration = view.findViewById(R.id.tvDuration);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return videos.size();
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            VideoItem video = videos.get(position);
            holder.tvTitle.setText(video.getTitle());
            holder.tvChannel.setText(video.getChannel());
            holder.tvDuration.setText(video.getDuration());
            holder.itemView.setOnClickListener(v -> onClick.onVideoClick(video));
        }
    }
}
