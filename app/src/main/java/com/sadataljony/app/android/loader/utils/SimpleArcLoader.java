package com.sadataljony.app.android.loader.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

import com.sadataljony.app.android.myapplication.R;

public class SimpleArcLoader extends View implements Animatable {
    private static final long FRAME_DURATION = 1000 / 60;
    public static int MARGIN_MEDIUM = 5;
    public static int SPEED_SLOW = 1;
    public static int SPEED_MEDIUM = 5;
    public static int SPEED_FAST = 10;

    public enum STYLE {SIMPLE_ARC, COMPLETE_ARC}

    ArcDrawable mArcDrawable;

    public SimpleArcLoader(Context context) {
        super(context);
    }

    public SimpleArcLoader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SimpleArcLoader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        LoaderConfiguration configuration = readFromAttributes(attributeSet);
        mArcDrawable = new ArcDrawable(configuration, this);
        setBackground(mArcDrawable);
        start();
    }

    private LoaderConfiguration readFromAttributes(AttributeSet attributeSet) {
        LoaderConfiguration configuration = new LoaderConfiguration(getContext());
        TypedArray array = getContext().obtainStyledAttributes(attributeSet, R.styleable.SimpleArcLoader);
        for (int i = 0; i < array.length(); i++) {
            int type = array.getIndex(i);

            if (type == R.styleable.SimpleArcLoader_arc_style) {
                String value = array.getString(R.styleable.SimpleArcLoader_arc_style);
                assert value != null;
                configuration.setLoaderStyle(STYLE.values()[Integer.parseInt(value)]);
            }

            if (type == R.styleable.SimpleArcLoader_arc_colors) {
                int colorsResourceId = array.getResourceId(R.styleable.SimpleArcLoader_arc_colors, 0);
                if (colorsResourceId != 0)
                    configuration.setColors(getContext().getResources().getIntArray(colorsResourceId));
            }

            if (type == R.styleable.SimpleArcLoader_arc_speed) {
                String value = array.getString(R.styleable.SimpleArcLoader_arc_speed);
                if (value != null)
                    configuration.setAnimationSpeedWithIndex(Integer.parseInt(value));
            }

            if (type == R.styleable.SimpleArcLoader_arc_margin) {
                float value = array.getDimension(R.styleable.SimpleArcLoader_arc_margin, MARGIN_MEDIUM);
                configuration.setArcMargin((int) value);
            }

            if (type == R.styleable.SimpleArcLoader_arc_thickness) {
                float value = array.getDimension(R.styleable.SimpleArcLoader_arc_thickness, getContext().getResources().getDimension(R.dimen.stroke_width));
                configuration.setArcWidthInPixel((int) value);
            }
        }
        array.recycle();
        return configuration;
    }

    @Override
    public void start() {
        if (mArcDrawable != null)
            mArcDrawable.start();
    }

    @Override
    public void stop() {
        if (mArcDrawable != null)
            mArcDrawable.stop();
    }

    @Override
    public boolean isRunning() {
        if (mArcDrawable != null)
            return mArcDrawable.isRunning();
        return false;
    }

    public void refreshArcLoaderDrawable(LoaderConfiguration configuration) {
        if (isRunning())
            stop();
        mArcDrawable = new ArcDrawable(configuration, this);
        setBackground(mArcDrawable);
        start();
    }

    private static class ArcDrawable extends Drawable implements Animatable {
        final Runnable updater = new Runnable() {
            @Override
            public void run() {
                mArcAnglePosition += mAnimationSpeed;
                if (mArcAnglePosition > 360)
                    mArcAnglePosition = 0;
                scheduleSelf(updater, FRAME_DURATION + SystemClock.uptimeMillis());
                invalidateSelf();
            }
        };

        LoaderConfiguration mConfiguration;
        Paint mPaint;
        int mStrokeWidth, mArcMargin, mArcAnglePosition, mAnimationSpeed;
        int[] mArcColors;
        boolean isRunning;
        boolean mDrawCircle;
        WeakReference<View> mViewReference;

        public ArcDrawable(LoaderConfiguration configuration, View viewReference) {
            mConfiguration = configuration;
            mViewReference = new WeakReference<>(viewReference);
            initComponents();
        }

        private void initComponents() {
            mStrokeWidth = mConfiguration.getArcWidth();
            mArcMargin = mConfiguration.getArcMargin();
            mArcColors = mConfiguration.getColors();
            mAnimationSpeed = mConfiguration.getAnimationSpeed();
            mDrawCircle = mConfiguration.drawCircle();

            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(mStrokeWidth);
            mPaint.setStyle(Paint.Style.STROKE);

            // Customize as per Style
            if (mConfiguration.getLoaderStyle() == STYLE.SIMPLE_ARC) {
                if (mArcColors.length > 1)
                    mArcColors = new int[]{mArcColors[0], mArcColors[0]};
            }
        }

        @Override
        public void start() {
            if (!isRunning()) {
                // Set the flag
                isRunning = true;
                scheduleSelf(updater, FRAME_DURATION + SystemClock.uptimeMillis());
                invalidateSelf();
            }
        }

        @Override
        public void stop() {
            if (isRunning()) {
                // Set the flag
                isRunning = false;
                unscheduleSelf(updater);
                invalidateSelf();
            }
        }

        @Override
        public boolean isRunning() {
            return isRunning;
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            View currentView = mViewReference.get();
            if (currentView == null)
                return;
            float w = currentView.getWidth();
            float h = currentView.getHeight();

            int arc1_bound_start = mArcMargin + mStrokeWidth * 2;
            int arc_padding = 0;

            if (mDrawCircle) {
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(Color.WHITE);
                canvas.drawCircle(w / 2, h / 2, w / 2, mPaint);

                // Reset the configuration
                mPaint.setStyle(Paint.Style.STROKE);

                // Add some padding
                arc_padding += 3;
            }

            RectF arc1_bound = new RectF(arc1_bound_start + arc_padding, arc1_bound_start + arc_padding, ((w - (mStrokeWidth * 2)) - mArcMargin) - arc_padding, ((h - (mStrokeWidth * 2)) - mArcMargin) - arc_padding);
            RectF arc2_bound = new RectF(mStrokeWidth + arc_padding, mStrokeWidth + arc_padding, (w - mStrokeWidth) - arc_padding, (h - mStrokeWidth) - arc_padding);
            int colors_length = mArcColors.length;

            for (int i = 0; i < (Math.min(colors_length, 4)); i++) {
                int startAngle = (90 * i);

                mPaint.setColor(mArcColors[i]);

                canvas.drawArc(arc1_bound, startAngle + mArcAnglePosition, 90, false, mPaint);
                canvas.drawArc(arc2_bound, startAngle - mArcAnglePosition, 90, false, mPaint);
            }
        }

        @Override
        public void setAlpha(int i) {
        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {
        }

        @Override
        public int getOpacity() {
            return PixelFormat.UNKNOWN;
        }
    }

}
