package com.excilys.shoofleurs.dashboard.displayables;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by buonomo on 29/03/16.
 */

/**
 * Cette interface permet d'afficher n'importe quel type de média (image, vidéo, pdf, etc.)
 * à l'interieur d'un layout.
 */
public abstract class AbstractDisplayable {
    public static final int INDETERMINED_TIME = -1;
    protected String mUrl;
    protected int mDurationInSec;

    public AbstractDisplayable(String url, int duration) {
        this.mUrl = url;
        this.mDurationInSec = duration;
    }
    /**
     * Cette fonction permet à un objet AbstractDisplayable de s'afficher à l'interieur
     * du layout parent passé en paramètre.
     * @param context
     * @param layout
     */
    public abstract int displayContent(Context context, ViewGroup layout);

    public int getDurationInSec() {
        return mDurationInSec;
    }

    @SuppressWarnings("unchecked")
    protected  <T extends View> T addOrReplaceViewByType(ViewGroup layout, Context context, Class<? extends T> valueType) {
        T t = null;
        if (layout.getChildCount() == 1) {
            if (layout.getChildAt(0).getClass().equals(valueType)) {
                t = (T) layout.getChildAt(0);
            }

            else {
                layout.removeViewAt(0);
                try {
                    t = valueType.getDeclaredConstructor(Context.class).newInstance(context);
                    addImageViewToLayout(t, layout);
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        /* Aucune view n'a encore été affectée au layout, on créé la NetworkImageView */
        else {
            try {
                t = valueType.getDeclaredConstructor(Context.class).newInstance(context);
                addImageViewToLayout(t, layout);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return t;
    }

    /**
     * Add the view to the layout with the layout params for matching layout size
     * @param imageView
     * @param layout
     */
    private void addImageViewToLayout(View imageView, ViewGroup layout) {
        ViewGroup.LayoutParams params =  new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        layout.addView(imageView);
    }
}
