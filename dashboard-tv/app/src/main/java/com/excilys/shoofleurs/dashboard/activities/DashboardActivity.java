package com.excilys.shoofleurs.dashboard.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.excilys.shoofleurs.dashboard.R;
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
    private ImageView mImageView;

    /**
     * Liste des configurations.
     */
    private List<JSONObject> mConfigList;

    /**
     * Index de l'image affichée en ce moment.
     */
    private int mCurrentBitmap = 0;

    /**
     * Création d'un interval entre les affichages.
     */
    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.diapo_images);
        mConfigList = new ArrayList<>();

    }

}
