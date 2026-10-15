package com.example.streamhub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import java.util.List;

public class ExploreFragment extends Fragment {

    private HapticManager haptic;
    private TtsManager tts;
    private List<VideoItem> allVideos;
    private RecyclerView rvSearchResults;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        haptic = new HapticManager(requireContext());
        tts = new TtsManager(requireContext());

        EditText etSearchQuery = view.findViewById(R.id.etSearchQuery);
        rvSearchResults = view.findViewById(R.id.rvSearchResults);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(requireContext()));

        allVideos = VideoRepository.getSampleVideos();

        updateList("");

        etSearchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void updateList(String query) {
        List<VideoItem> filtered = new ArrayList<>();
        if (query == null || query.isEmpty()) {
            filtered = allVideos;
        } else {
            String lower = query.toLowerCase();
            for (VideoItem item : allVideos) {
                if (item.getTitle().toLowerCase().contains(lower) ||
                        item.getCategory().toLowerCase().contains(lower) ||
                        item.getChannel().toLowerCase().contains(lower)) {
                    filtered.add(item);
                }
            }
        }
        rvSearchResults.setAdapter(new HomeFragment.VideoAdapter(filtered, video -> {
            haptic.playClickPulse();
            tts.speak("Playing " + video.getTitle());
            Intent intent = new Intent(requireContext(), PlayerActivity.class);
            intent.putExtra("video", video);
            startActivity(intent);
        }));
    }
}
