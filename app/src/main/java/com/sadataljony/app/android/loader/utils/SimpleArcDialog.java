package com.sadataljony.app.android.loader.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;

import com.sadataljony.app.android.myapplication.R;

import java.util.Objects;

public class SimpleArcDialog extends Dialog {
    private LoaderConfiguration mConfiguration;

    public SimpleArcDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.layout_loader);

        SimpleArcLoader mLoaderView = findViewById(R.id.loader);
        LinearLayout mLayout = findViewById(R.id.llLoader);

        if (mConfiguration != null) {
            mLoaderView.refreshArcLoaderDrawable(mConfiguration);
        }

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.WHITE);
        gd.setCornerRadius(10);
        gd.setStroke(2, Color.WHITE);
        mLayout.setBackground(gd);
    }

    public void setConfiguration(LoaderConfiguration configuration) {
        mConfiguration = configuration;
    }

}
