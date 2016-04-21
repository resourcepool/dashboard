package com.excilys.shoofleurs.dashboard.activities;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;

import com.excilys.shoofleurs.dashboard.R;
import com.excilys.shoofleurs.dashboard.requests.Download;
import com.excilys.shoofleurs.dashboard.requests.Get;
import com.excilys.shoofleurs.dashboard.requests.ICallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
public class MainActivity extends AppCompatActivity implements ICallback {
    /**
     * Affichage des images.
     */
    private ImageView mImageView;

    /**
     * Cache.
     */
    private List<Bitmap> mBitmapList;

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

    /**
     * Liste de Flash News à afficher
     */
    private List<String> mMessages = Arrays.asList("Un premier message un peu long - Un premier message un peu long - Un premier message un peu long", "un autre pas trop long", "et encore un aautre pas trop long");

    /**
     * Iterateur de liste de flash news
     */
    private Iterator<String> mMessagesIterator;

    /**
     * TextView de la News
     */
    private TextView mTextView;

    /**
     * Animation du scroll du texte
     */
    private Scroller mScroller;

    /**
     * Ordre du prochain message à afficher
     */
    private final Runnable mNextMessageRunnable = new Runnable() {
        @Override
        public void run() {
            nextMessage();
        }
    };

    /**
     * Taille approximative de l'écran
     */
    private int mSizeScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        mSizeScreen = point.x;

//        mImageView = (ImageView) findViewById(R.id.current_content_layout);
        mBitmapList = new ArrayList<>();
        mConfigList = new ArrayList<>();

        // Récupération de la liste des contenus du diaporamas numéro 1 (temporairement, un
        // Json Array contenant des Strings. (cf serveur side)
//        new Get<String[]>(this, String[].class, 1).execute();


        // Initialisation pour les flash news
        mScroller = new Scroller(this, new LinearInterpolator());
        mTextView = (TextView) findViewById(R.id.text_cnn);
        mTextView.setScroller(mScroller);
        nextMessage();
    }

    /**
     * Appelle l'affichage du prochaine message
     */
    private void nextMessage() {
        if (mMessagesIterator == null || !mMessagesIterator.hasNext()) {
            mMessagesIterator = mMessages.iterator();
        }
        String message;

        // S'il n'ya pas plus de messages
        if (!mMessagesIterator.hasNext()) {
            return;
        }

        // Sinon, affiche le message
        message = mMessagesIterator.next();

        Rect bounds = new Rect();
        mTextView.getPaint().getTextBounds(message, 0, message.length(), bounds);
        mTextView.setText(message);

        int durationScroll = (bounds.right+ mSizeScreen)*1000 / 300;
        mScroller.startScroll(-mSizeScreen, 0, bounds.right+ mSizeScreen, 0, durationScroll);

        mHandler.postDelayed(mNextMessageRunnable, durationScroll);
    }


    @Override
    public void asyncTaskFinish(Object result, int code) {
        // Mise en cache du contenu de la liste (code identifiant requete)
        if (code == 1) {
            String[] results = (String[]) result;
            try {
                // Convertion au format Json Object pour accéder aux propriétés.
                // Temporairement, création d'une liste pour stocker les configurations.
                for (String jsonObjectAsString: results) {
                    JSONObject jsonObject = new JSONObject(jsonObjectAsString);
                    mConfigList.add(jsonObject.getJSONObject("config"));
                    // Téléchargement des images.
                    new Download(2, this, jsonObject.getString("chemin")).execute();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Une fois le téléchargement d'une image terminée ...
        else if (code == 2) {
            if (result != null) {
                Bitmap img = (Bitmap) result;
                // On l'ajoute à la liste du cache
                mBitmapList.add(img);

                // Si c'est la première image du diaporama, on l'affiche dessuite.
                // Les autres images sont en cours de téléchargements. Dès qu'une image a fini de
                // télécharger, elle est ajoutée automatiquement au cache. C'est ensuite le
                // handler (voir ci-dessous) qui va gérer le changement dans l'affichage.
                if (mImageView.getDrawable() == null) {
                    mImageView.setImageBitmap(img);
                    try {
                        // On indique au handler l'index de la prochaine image qu'il devra afficher
                        ++mCurrentBitmap;
                        // et on lui indique dans combien de temps (eg la durée de l'image affichée
                        // en ce moment en millisecondes.
                        next(mConfigList.get(mCurrentBitmap - 1).getInt("duration") * 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    /**
     * Cette méthode permet de gérer l'affichage par intervalle des photos.
     * @param timeInMs Temps avant d'afficher la prochaine photo
     */
    public void next(int timeInMs) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // On teste d'abord si le cache est bien rempli afin d'éviter une
                // exception OutOfBounds. Cela permet aussi d'attendre éventuellement que
                // le prochain fichier soit sauvegardé en cache.
                if (mCurrentBitmap >= mBitmapList.size()) mCurrentBitmap = 0;
                // On affiche la nouvelle image
                mImageView.setImageBitmap(mBitmapList.get(mCurrentBitmap));
                try {
                    // et on relance le délais pour l'affichage de la prochaine image avec
                    // l'éventuelle nouvelle valeur d'attente.
                    ++mCurrentBitmap;
                    next(mConfigList.get(mCurrentBitmap - 1).getInt("duration") * 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, timeInMs);
    }
}
