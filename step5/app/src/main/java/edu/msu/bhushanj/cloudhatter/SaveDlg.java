package edu.msu.bhushanj.cloudhatter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by jaiwant on 11/3/2016.
 */
public class SaveDlg extends DialogFragment{

    private AlertDialog dlg;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        // Set the title
        builder.setTitle(R.string.Save_title);

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Pass null as the parent view because its going in the dialog layout
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.save_dlg, null);
        builder.setView(view);


        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                EditText editName = (EditText)dlg.findViewById(R.id.editName);
                // do something with editName...
                save(editName.getText().toString());
            }
        });

        // Add a cancel button
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // Cancel just closes the dialog box
            }
        });

        // Create the dialog box
        dlg = builder.create();
        return dlg;

    }

    /**
     * Actually save the hatting
     * @param name name to save it under
     */
    private void save(final String name) {
        if (!(getActivity() instanceof HatterActivity)) {
            return;
        }
        final HatterActivity activity = (HatterActivity) getActivity();
        final HatterView view = (HatterView) activity.findViewById(R.id.hatterView);

        Cloud cloud = new Cloud();
        final boolean ok = cloud.saveToCloud(name, view);
        if(!ok) {
                    /*
                     * If we fail to save, display a toast
                     */
            // Please fill this in...
            Toast toast = Toast.makeText(getActivity(),"Failed to connect to the server", Toast.LENGTH_LONG);
            toast.show();

        }
    }

}
