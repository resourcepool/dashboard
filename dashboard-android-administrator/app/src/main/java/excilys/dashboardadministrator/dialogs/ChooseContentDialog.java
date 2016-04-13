package excilys.dashboardadministrator.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import excilys.dashboardadministrator.R;

/**
 * Created by buonomo on 12/04/16.
 */
public class ChooseContentDialog extends DialogFragment{
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View rootView = inflater.inflate(R.layout.dialog_choose_content, null);

        LinearLayout chooseImageLayout = (LinearLayout) rootView.findViewById(R.id.dialog_choose_content_image_layout);
        chooseImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
            }
        });

        LinearLayout chooseVideoLayout = (LinearLayout) rootView.findViewById(R.id.dialog_choose_content_video_layout);
        chooseImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(rootView);
        return builder.create();
    }
}
