package io.resourcepool.dashboard.ui.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import io.resourcepool.dashboard.R;

public class TypeFaceTextView extends TextView {
    private static final String TAG = "TextView";

    public TypeFaceTextView(Context context) {
        super(context);
    }

    public TypeFaceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TypeFaceTextView, 0, 0);
        setCustomFont(context, a.getString(R.styleable.TypeFaceTextView_typeface));
        a.recycle();
    }

    public TypeFaceTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TypeFaceTextView, 0, 0);
        setCustomFont(context, a.getString(R.styleable.TypeFaceTextView_typeface));
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String typeface) {
        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(ctx.getAssets(), typeface);
        } catch (Exception e) {
            Log.e(TAG, "Could not get typeface: " + e.getMessage());
            return false;
        }

        setTypeface(tf);
        return true;
    }

}