package com.example.delteutgifter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

/**
 * Created by:
 * User: Einar
 * Date: 11.03.13
 * Time: 19:34
 */
public class SetNamesDialog extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface SetNamesDialogListener {
        public void onSetNamesDialogPositiveClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    SetNamesDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the SetNamesDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the SetNamesDialogListener so we can send events to the host
            mListener = (SetNamesDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement SetNamesDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_set_names, null));


        builder
                // Setting dialog title
                .setTitle(R.string.setNames_message)

                        // Adding "Save names" button
                .setPositiveButton(R.string.saveNames, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onSetNamesDialogPositiveClick(SetNamesDialog.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
