package com.excilys.shoofleurs.dashboard.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.excilys.shoofleurs.dashboard.R;
import com.excilys.shoofleurs.dashboard.requests.Download;
import com.excilys.shoofleurs.dashboard.requests.Get;
import com.excilys.shoofleurs.dashboard.requests.ICallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {
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


//    @Override
//    public void asyncTaskFinish(Object result, int code) {
//        // Mise en cache du contenu de la liste (code identifiant requete)
//        if (code == 1) {
//            String[] results = (String[]) result;
//            try {
//                // Convertion au format Json Object pour accéder aux propriétés.
//                // Temporairement, création d'une liste pour stocker les configurations.
//                for (String jsonObjectAsString: results) {
//                    JSONObject jsonObject = new JSONObject(jsonObjectAsString);
//                    mConfigList.add(jsonObject.getJSONObject("config"));
//                    // Téléchargement des images.
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        // Une fois le téléchargement d'une image terminée ...
//        else if (code == 2) {
//            if (result != null) {
//                Bitmap img = (Bitmap) result;
//                // On l'ajoute à la liste du cache
//                mBitmapList.add(img);
//
//                // Si c'est la première image du diaporama, on l'affiche dessuite.
//                // Les autres images sont en cours de téléchargements. Dès qu'une image a fini de
//                // télécharger, elle est ajoutée automatiquement au cache. C'est ensuite le
//                // handler (voir ci-dessous) qui va gérer le changement dans l'affichage.
//                if (mImageView.getDrawable() == null) {
//                    mImageView.setImageBitmap(img);
//                    try {
//                        // On indique au handler l'index de la prochaine image qu'il devra afficher
//                        ++mCurrentBitmap;
//                        // et on lui indique dans combien de temps (eg la durée de l'image affichée
//                        // en ce moment en millisecondes.
//                        next(mConfigList.get(mCurrentBitmap - 1).getInt("duration") * 1000);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }


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
