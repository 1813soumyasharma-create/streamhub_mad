package com.example.streamhub.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

public class VideoCanvasView extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path path = new Path();
    private float phase = 0f;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            phase += 0.08f;
            invalidate();
            handler.postDelayed(this, 16); // ~60 FPS smooth video playback rendering
        }
    };

    public VideoCanvasView(Context context) {
        super(context);
        init();
    }

    public VideoCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VideoCanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        handler.post(runnable);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth();
        float height = getHeight();

        // 1. Dynamic Video Background Frame
        paint.setColor(0xFF0F172A);
        canvas.drawRect(0f, 0f, width, height, paint);

        // 2. Animated Video Visualizer Waves (Simulating video motion)
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xFF2563EB);

        path.reset();
        path.moveTo(0f, height * 0.65f);
        float x = 0f;
        while (x <= width) {
            float y = height * 0.65f + (float) Math.sin(x * 0.015f + phase) * 45f + (float) Math.cos(phase * 1.5f) * 25f;
            path.lineTo(x, y);
            x += 8f;
        }
        path.lineTo(width, height);
        path.lineTo(0f, height);
        path.close();
        canvas.drawPath(path, paint);

        // Second wave layer for depth
        paint.setColor(0x8038BDF8);
        path.reset();
        path.moveTo(0f, height * 0.75f);
        x = 0f;
        while (x <= width) {
            float y = height * 0.75f + (float) Math.cos(x * 0.02f - phase) * 35f;
            path.lineTo(x, y);
            x += 8f;
        }
        path.lineTo(width, height);
        path.lineTo(0f, height);
        path.close();
        canvas.drawPath(path, paint);

        // 3. Floating Video Motion Elements
        paint.setColor(0xFF38BDF8);
        float cx = width * 0.5f + (float) Math.sin(phase * 0.8f) * (width * 0.3f);
        float cy = height * 0.35f + (float) Math.cos(phase * 0.6f) * (height * 0.15f);
        canvas.drawCircle(cx, cy, 40f, paint);

        paint.setColor(0xFFF59E0B);
        float cx2 = width * 0.3f + (float) Math.cos(phase * 1.1f) * (width * 0.25f);
        float cy2 = height * 0.45f + (float) Math.sin(phase * 0.7f) * (height * 0.15f);
        canvas.drawCircle(cx2, cy2, 30f, paint);

        // 4. Playing Watermark / Timecode
        paint.setColor(0xAA000000);
        canvas.drawRect(20f, height - 60f, 220f, height - 20f, paint);

        paint.setColor(0xFFFFFFFF);
        paint.setTextSize(26f);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("▶ 01:24 / 04:00", 35f, height - 32f, paint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacks(runnable);
    }
}
