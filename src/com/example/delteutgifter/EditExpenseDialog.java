package com.example.delteutgifter;

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
    int expenseId;

    public void setExpense(int id) {
        this.expense = Expense.getAllExpenses().get(id);
        this.expenseId = id;
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

                        // Used to save changes
                .setPositiveButton(R.string.save_changes_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText amountEditText = (EditText) EditExpenseDialog.this.getDialog().findViewById(R.id.edit_amount);
                        EditText descriptionEditText = (EditText) EditExpenseDialog.this.getDialog().findViewById(R.id.description);
                        Expense.getAllExpenses().get(expenseId).setAmount(Float.parseFloat(amountEditText.getText().toString()));
                        Expense.getAllExpenses().get(expenseId).setDescription(descriptionEditText.getText().toString());
                    }
                })

                        // Used to delete entry
                .setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Expense.deleteExpense(expenseId);
                        MainActivity.forceUpdateListView();
                    }
                })

                        // Used to cancel
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditExpenseDialog.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
