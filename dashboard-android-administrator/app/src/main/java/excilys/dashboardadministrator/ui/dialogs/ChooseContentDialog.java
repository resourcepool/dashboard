package excilys.dashboardadministrator.ui.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import excilys.dashboardadministrator.R;

public class ChooseContentDialog extends DialogFragment {
    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int PICK_VIDEO_REQUEST = 2;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View rootView = inflater.inflate(R.layout.dialog_choose_content, null);
        Log.i(getClass().getSimpleName(), "onCreateDialog!: ");

        LinearLayout chooseImageLayout = (LinearLayout) rootView.findViewById(R.id.dialog_choose_content_image_layout);

        chooseImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                getActivity().startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
                dismiss();
            }
        });


        LinearLayout chooseVideoLayout = (LinearLayout) rootView.findViewById(R.id.dialog_choose_content_video_layout);
        chooseVideoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                getActivity().startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST);
                dismiss();
            }
        });

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(rootView);
        return builder.create();
    }
}
