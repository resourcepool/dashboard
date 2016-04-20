package com.excilys.shoofleurs.dashboard.activities;

import android.animation.AnimatorSet;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.excilys.shoofleurs.dashboard.R;
import com.excilys.shoofleurs.dashboard.factory.AnimatorFactory;
import com.excilys.shoofleurs.dashboard.managers.DiaporamaManager;
import com.excilys.shoofleurs.dashboard.model.entities.Diaporama;
import com.excilys.shoofleurs.dashboard.requests.Get;
import com.excilys.shoofleurs.dashboard.requests.ICallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * L'activité principale récupère la liste des contenus. Dans ce scénario, elle récupère
 * les contenus du diaporama d'ID 1 (qui contient deux images) via l'AsyncTask {@link Get}
 * Une fois récupérée, les contenus sont mis en cache puis affichés via un handler qui va
 * gérer le temps d'affichage entre eux.
 *
 * Cette activité implémente l'interface {@link ICallback} qui permet à une AsyncTask de notifier
 * à l'activité qu'elle a fini son travail et que l'activité peut traiter le résultat comme elle
 * le souhaite. Le code passé en paramètre permet d'identifier le type d'action à effectuer et
 * le type du résultat.
 */
public class DashboardActivity extends AppCompatActivity {
    /**
     * Affichage des images.
     */
    private RelativeLayout mContentLayout;

    private AnimatorSet mProgressAnimatorSet1, mProgressAnimatorSet2;

    /**
     * Liste des configurations.
     */
    private List<JSONObject> mConfigList;

    /**
     * Index de l'image affichée en ce moment.
     */
    private int mCurrentBitmap = 0;

    /**
     * Le diaporama courant qui doit être affiché
     */
    private Diaporama mCurrentDiaporama;

    /**
     * Création d'un interval entre les affichages.
     */
    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startProgressView();
        DiaporamaManager.getInstance(this).checkUpdates();
        mContentLayout = (RelativeLayout) findViewById(R.id.current_content_layout);
        mConfigList = new ArrayList<>();
    }

    public void startProgressView() {
        View point1 = findViewById(R.id.progress_view_point1);
        View point2 = findViewById(R.id.progress_view_point2);
        RelativeLayout progressViewLayout = (RelativeLayout) findViewById(R.id.progress_view_layout);
        mProgressAnimatorSet1 = AnimatorFactory.createProgressPointAnimatorSet(point1, progressViewLayout.getLayoutParams().width);
        mProgressAnimatorSet2 = AnimatorFactory.createProgressPointAnimatorSet(point2, progressViewLayout.getLayoutParams().width);

        mProgressAnimatorSet1.start();
        mProgressAnimatorSet2.setStartDelay(1000);
        mProgressAnimatorSet2.start();
    }

    public void stopProgressView() {
        if (mProgressAnimatorSet1 != null) {
            mProgressAnimatorSet1.cancel();
        }
        if (mProgressAnimatorSet2 != null) {
            mProgressAnimatorSet2.cancel();
        }
    }


    public Diaporama getCurrentDiaporama() {
        return mCurrentDiaporama;
    }


    public void setCurrentDiaporama(Diaporama mCurrentDiaporama) {
        this.mCurrentDiaporama = mCurrentDiaporama;
        Log.i(DashboardActivity.class.getSimpleName(), "setCurrentDiaporama: "+mCurrentDiaporama);
        startDiaporama();
    }

    public void startDiaporama() {
        stopProgressView();


    }
}
