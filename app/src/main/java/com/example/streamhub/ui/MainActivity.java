package com.example.streamhub.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.streamhub.R;
import com.example.streamhub.accessibility.ColorVisionHelper;
import com.example.streamhub.accessibility.HapticManager;
import com.example.streamhub.accessibility.TtsManager;
import com.example.streamhub.data.PreferencesManager;
import com.example.streamhub.receiver.OfflineReceiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private HapticManager haptic;
    private TtsManager tts;
    private OfflineReceiver offlineReceiver;
    private PreferencesManager prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = new PreferencesManager(this);
        haptic = new HapticManager(this);
        tts = new TtsManager(this);

        ColorVisionHelper.applyToActivity(this, prefs.getColorVisionMode());

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        bottomNav.setOnItemSelectedListener(item -> {
            haptic.playClickPulse();
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                loadFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.nav_explore) {
                loadFragment(new ExploreFragment());
                return true;
            } else if (itemId == R.id.nav_library) {
                loadFragment(new LibraryFragment());
                return true;
            } else if (itemId == R.id.nav_map) {
                startActivity(new Intent(this, CommunityMapActivity.class));
                return false;
            } else if (itemId == R.id.nav_settings) {
                loadFragment(new SettingsFragment());
                return true;
            }
            return false;
        });

        offlineReceiver = new OfflineReceiver(isConnected -> {
            if (!isConnected) {
                tts.speak("Offline banner active.");
            }
        });
        @SuppressWarnings("deprecation")
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(offlineReceiver, filter);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (offlineReceiver != null) {
            unregisterReceiver(offlineReceiver);
        }
    }
}
