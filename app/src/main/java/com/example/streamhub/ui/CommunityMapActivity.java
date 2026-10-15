package com.example.streamhub.ui;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.streamhub.R;
import com.example.streamhub.accessibility.ColorVisionHelper;
import com.example.streamhub.accessibility.HapticManager;
import com.example.streamhub.accessibility.TtsManager;
import com.example.streamhub.data.PreferencesManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CommunityMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private HapticManager haptic;
    private TtsManager ttsManager;

    private static final LatLng ASL_CAFE = new LatLng(37.7749, -122.4194);
    private static final LatLng DEAF_CENTER = new LatLng(37.7833, -122.4167);
    private static final LatLng BRAILLE_LIBRARY = new LatLng(37.7692, -122.4467);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_map);

        PreferencesManager prefs = new PreferencesManager(this);
        haptic = new HapticManager(this);
        ttsManager = new TtsManager(this, () -> ttsManager.speak("Google Community Map loaded. 3 accessible venues nearby."));

        ColorVisionHelper.applyToActivity(this, prefs.getColorVisionMode());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        TextView venue1 = findViewById(R.id.venue1);
        TextView venue2 = findViewById(R.id.venue2);
        TextView venue3 = findViewById(R.id.venue3);

        venue1.setOnClickListener(v -> {
            haptic.playSuccessPulse();
            ttsManager.speak("ASL Cafe selected. 0.4 kilometers away with signing staff available.");
            if (googleMap != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ASL_CAFE, 16f));
            }
        });

        venue2.setOnClickListener(v -> {
            haptic.playSuccessPulse();
            ttsManager.speak("Deaf Community Center selected. 1.1 kilometers away with event Saturday at 5 PM.");
            if (googleMap != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(DEAF_CENTER, 16f));
            }
        });

        venue3.setOnClickListener(v -> {
            haptic.playSuccessPulse();
            ttsManager.speak("Braille Library selected. 2.3 kilometers away with audio guides.");
            if (googleMap != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(BRAILLE_LIBRARY, 16f));
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        this.googleMap = map;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        googleMap.addMarker(new MarkerOptions()
                .position(ASL_CAFE)
                .title("ASL Cafe")
                .snippet("0.4 km - Signing staff available"));

        googleMap.addMarker(new MarkerOptions()
                .position(DEAF_CENTER)
                .title("Deaf Community Center")
                .snippet("1.1 km - Event Sat 5 PM"));

        googleMap.addMarker(new MarkerOptions()
                .position(BRAILLE_LIBRARY)
                .title("Braille Library")
                .snippet("2.3 km - Audio guides"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ASL_CAFE, 14f));

        googleMap.setOnMarkerClickListener(marker -> {
            haptic.playSuccessPulse();
            ttsManager.speak(marker.getTitle() + ". " + marker.getSnippet());
            return false;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ttsManager != null) {
            ttsManager.shutdown();
        }
    }
}
