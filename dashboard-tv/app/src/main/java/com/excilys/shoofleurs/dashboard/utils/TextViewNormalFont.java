package com.excilys.shoofleurs.dashboard.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class TextViewNormalFont extends TextView {
    private static final String TAG = "TextView";

    public TextViewNormalFont(Context context) {
        super(context);
    }

    public TextViewNormalFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context);
    }

    public TextViewNormalFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context);
    }

    public boolean setCustomFont(Context ctx) {
        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(ctx.getAssets(), "Roboto-Light.ttf");
        } catch (Exception e) {
            Log.e(TAG, "Could not get typeface: " + e.getMessage());
            return false;
        }

        setTypeface(tf);
        return true;
    }

}