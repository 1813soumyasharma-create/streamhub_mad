package com.example.streamhub.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

public class AslInterpreterCanvasView extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path path = new Path();
    private float phase = 0f;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            phase += 0.1f;
            invalidate();
            handler.postDelayed(this, 35);
        }
    };

    public AslInterpreterCanvasView(Context context) {
        super(context);
        init();
    }

    public AslInterpreterCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AslInterpreterCanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
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

        // Background
        paint.setColor(0xFF1E293B);
        canvas.drawRect(0f, 0f, width, height, paint);

        // Draw animated ASL interpreter silhouette / hands signing motion
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xFF38BDF8);

        // Head
        canvas.drawCircle(width * 0.5f, height * 0.3f, 14f, paint);

        // Body / Shoulders
        path.reset();
        path.moveTo(width * 0.3f, height * 0.8f);
        path.quadTo(width * 0.5f, height * 0.5f, width * 0.7f, height * 0.8f);
        path.lineTo(width * 0.8f, height);
        path.lineTo(width * 0.2f, height);
        path.close();
        canvas.drawPath(path, paint);

        // Animated hands signing gestures
        paint.setColor(0xFFF59E0B);
        float handLeftX = width * 0.35f + (float) Math.sin(phase) * 12f;
        float handLeftY = height * 0.55f + (float) Math.cos(phase * 1.2f) * 10f;
        canvas.drawCircle(handLeftX, handLeftY, 9f, paint);

        float handRightX = width * 0.65f + (float) Math.cos(phase) * 12f;
        float handRightY = height * 0.55f + (float) Math.sin(phase * 1.2f) * 10f;
        canvas.drawCircle(handRightX, handRightY, 9f, paint);

        // Pre-recorded ASL badge text
        paint.setColor(0xFFFFFFFF);
        paint.setTextSize(16f);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("ASL Pre-recorded", width * 0.5f, height - 6f, paint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacks(runnable);
    }
}
