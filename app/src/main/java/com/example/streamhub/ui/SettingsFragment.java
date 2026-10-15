package com.example.streamhub.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.streamhub.R;
import com.example.streamhub.accessibility.HapticManager;
import com.example.streamhub.accessibility.TtsManager;
import com.example.streamhub.data.PreferencesManager;

public class SettingsFragment extends Fragment {

    private PreferencesManager prefs;
    private HapticManager haptic;
    private TtsManager tts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        prefs = new PreferencesManager(requireContext());
        haptic = new HapticManager(requireContext());
        tts = new TtsManager(requireContext());

        Button btnOpenAdvanced = view.findViewById(R.id.btnOpenAdvanced);
        Button btnHelp = view.findViewById(R.id.btnHelp);
        Button btnAbout = view.findViewById(R.id.btnAbout);
        Button btnLogout = view.findViewById(R.id.btnLogout);

        btnOpenAdvanced.setOnClickListener(v -> {
            haptic.playClickPulse();
            tts.speak("Opening Advanced Accessibility Settings");
            startActivity(new Intent(requireContext(), AccessibilitySettingsActivity.class));
        });

        btnHelp.setOnClickListener(v -> {
            haptic.playClickPulse();
            startActivity(new Intent(requireContext(), HelpActivity.class));
        });

        btnAbout.setOnClickListener(v -> {
            haptic.playClickPulse();
            startActivity(new Intent(requireContext(), AboutActivity.class));
        });

        btnLogout.setOnClickListener(v -> {
            haptic.playAlertPulse();
            new AlertDialog.Builder(requireContext())
                    .setTitle("Log out")
                    .setMessage("Are you sure you want to log out of StreamHub?")
                    .setPositiveButton("Log out", (dialog, which) -> {
                        prefs.setLoggedIn(false);
                        Intent intent = new Intent(requireContext(), LoginRegisterActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        return view;
    }
}
