package com.example.streamhub.accessibility;

import android.app.Activity;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.View;
import android.widget.ImageView;

public class ColorVisionHelper {

    public enum VisionMode {
        NORMAL,
        PROTANOPIA,      // Red-blind
        DEUTERANOPIA,    // Green-blind
        TRITANOPIA,      // Blue-blind
        HIGH_CONTRAST
    }

    public static void applyToActivity(Activity activity, String modeString) {
        View root = activity.getWindow().getDecorView().getRootView();
        if (modeString != null) {
            String lower = modeString.toLowerCase();
            if (lower.contains("high contrast")) {
                root.setBackgroundColor(0xFF000000);
            } else if (lower.contains("protanopia")) {
                root.setBackgroundColor(0xFF111827);
            } else if (lower.contains("deuteranopia")) {
                root.setBackgroundColor(0xFF1A103C);
            } else if (lower.contains("tritanopia")) {
                root.setBackgroundColor(0xFF0A2E36);
            } else {
                root.setBackgroundColor(0xFF0F172A);
            }
        } else {
            root.setBackgroundColor(0xFF0F172A);
        }
    }

    public static void applyVisionFilter(View view, VisionMode mode) {
        ColorMatrix matrix = getSimulationMatrix(mode);
        if (view instanceof ImageView) {
            ((ImageView) view).setColorFilter(new ColorMatrixColorFilter(matrix));
        }
    }

    public static ColorMatrix getSimulationMatrix(VisionMode mode) {
        ColorMatrix matrix = new ColorMatrix();
        switch (mode) {
            case NORMAL:
                matrix.reset();
                break;
            case PROTANOPIA:
                matrix.set(new float[]{
                        0.56667f, 0.43333f, 0.0f, 0.0f, 0.0f,
                        0.55833f, 0.44167f, 0.0f, 0.0f, 0.0f,
                        0.0f,      0.24167f, 0.75833f, 0.0f, 0.0f,
                        0.0f,      0.0f,      0.0f,      1.0f, 0.0f
                });
                break;
            case DEUTERANOPIA:
                matrix.set(new float[]{
                        0.625f, 0.375f, 0.0f, 0.0f, 0.0f,
                        0.7f,   0.3f,   0.0f, 0.0f, 0.0f,
                        0.0f,   0.3f,   0.7f, 0.0f, 0.0f,
                        0.0f,   0.0f,   0.0f,   1.0f, 0.0f
                });
                break;
            case TRITANOPIA:
                matrix.set(new float[]{
                        0.95f, 0.05f, 0.0f, 0.0f, 0.0f,
                        0.0f,  0.433f, 0.567f, 0.0f, 0.0f,
                        0.0f,  0.475f, 0.525f, 0.0f, 0.0f,
                        0.0f,  0.0f,   0.0f,   1.0f, 0.0f
                });
                break;
            case HIGH_CONTRAST:
                float scale = 1.4f;
                float translate = -0.2f * 255.0f * (scale - 1.0f);
                matrix.set(new float[]{
                        scale, 0.0f, 0.0f, 0.0f, translate,
                        0.0f, scale, 0.0f, 0.0f, translate,
                        0.0f, 0.0f, scale, 0.0f, translate,
                        0.0f, 0.0f, 0.0f, 1.0f, 0.0f
                });
                break;
        }
        return matrix;
    }
}
