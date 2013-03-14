package com.example.delteutgifter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by:
 * User: Einar
 * Date: 14.03.13
 * Time: 16:50
 */
public class EditExpenseDialog extends DialogFragment {

    // Expense being edited
    Expense expense;

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface EditExpenseDialogListener {
        public void onEditExpenseDialogPositiveClick(DialogFragment dialog);

        public void onEditExpenseDialogNeutralClick(DialogFragment dialog);
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public Expense getExpense;

    EditExpenseDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the SetNamesDialogListener so we can send events to the host
            mListener = (EditExpenseDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement EditExpenseDialogListener");
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
        View dialogView = inflater.inflate(R.layout.dialog_edit_expense, null);
        builder.setView(dialogView);

        EditText amountEditText = (EditText) dialogView.findViewById(R.id.edit_amount);
        amountEditText.setText(Float.toString(expense.getAmount()));

        EditText descriptionEditText = (EditText) dialogView.findViewById(R.id.description);
        descriptionEditText.setText(expense.getDescription());

        String titleString = "Edit Expense " + "(" + expense.getTimestampString() + ")";

        builder
                // Setting dialog title
                .setTitle(titleString)

                .setPositiveButton(R.string.save_changes_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onEditExpenseDialogPositiveClick(EditExpenseDialog.this);
                    }
                })

                .setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onEditExpenseDialogNeutralClick(EditExpenseDialog.this);
                    }
                })

                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditExpenseDialog.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}