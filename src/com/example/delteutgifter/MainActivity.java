package com.example.delteutgifter;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.*;

import java.util.*;

public class MainActivity extends Activity implements SetNamesDialog.NoticeDialogListener {

    String person1Name = null;
    String person2Name = null;
    float paidByPerson1 = 0;
    float paidByPerson2 = 0;
    boolean namesSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    protected void onStart() {
        super.onStart();

        // Restoring saved names and expenses
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        person1Name = sharedPref.getString("person1Name", null);
        person2Name = sharedPref.getString("person2Name", null);
        namesSet = sharedPref.getBoolean("namesSet", false);
        String serializedExpenses = sharedPref.getString("expenses", "");
        Expense.deserializeExpenses(serializedExpenses);
        setButtonNames();

        // Setting names if they have not previously been set
        if (!namesSet) {
            // Showing the "Set Names" dialog
            DialogFragment dialog = new SetNamesDialog();
            dialog.show(getFragmentManager(), "SetNamesDialogFragment");
        }

        calculateNewBalances();
        displayBalance();
        initializeExpenseLog();


    }

    private void initializeExpenseLog() {
        //initList();
        initializeExpenseList();

        // We get the ListView component from the layout
        ListView lv = (ListView) findViewById(R.id.logView);

        /**
         * This is a simple adapter that accepts as parameter
         * Context
         * Data list
         * The row layout that is used during the row creation
         * The keys used to retrieve the data
         * The View id used to show the data. The key number and the view id must match
         */

        SimpleAdapter simpleAdpt = new SimpleAdapter(this, expenseList, android.R.layout.simple_list_item_2, new String[]{"expense"}, new int[]{android.R.id.text2});
        lv.setAdapter(simpleAdpt);


    }

    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("expenses", Expense.serializeExpenses());
        editor.commit();

    }

    private void setButtonNames() {
        Button person1Button = (Button) findViewById(R.id.person1button);
        Button person2Button = (Button) findViewById(R.id.person2button);
        person1Button.setText(person1Name);
        person2Button.setText(person2Name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void paidByPerson1(View view) {
        EditText editText = (EditText) findViewById(R.id.ETenterAmount);
        float amountPaid;
        try {
            amountPaid = Float.parseFloat(editText.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }
        Calendar timestamp = Calendar.getInstance();

        Expense expense = new Expense(person1Name, amountPaid, timestamp);
        Expense.addExpense(expense);

        calculateNewBalances();
        initializeExpenseList();
        ((EditText) findViewById(R.id.ETenterAmount)).setText("");
    }

    public void paidByPerson2(View view) {
        EditText editText = (EditText) findViewById(R.id.ETenterAmount);
        float amountPaid;
        try {
            amountPaid = Float.parseFloat(editText.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }
        Calendar timestamp = Calendar.getInstance();

        Expense expense = new Expense(person2Name, amountPaid, timestamp);
        Expense.addExpense(expense);

        calculateNewBalances();
        initializeExpenseList();
        ((EditText) findViewById(R.id.ETenterAmount)).setText("");
    }

    public void calculateNewBalances() {
        paidByPerson1 = 0;
        paidByPerson2 = 0;

        ArrayList<Expense> expenses = Expense.getAllExpenses();

        for (Expense expense : expenses) {
            if (expense.getName().equals(person1Name)) {
                paidByPerson1 += expense.getAmount();
            } else if (expense.getName().equals(person2Name)) {
                paidByPerson2 += expense.getAmount();
            }
        }

        displayBalance();
    }

    public void displayBalance() {
        double balance = (paidByPerson1 - paidByPerson2) / 2;
        String balanceText;
        if (balance < 0) {
            balanceText = person1Name + " owes: " + (-balance) + ",-";
        } else if (balance > 0) {
            balanceText = person2Name + " owes: " + (balance) + ",-";
        } else {
            balanceText = "You're even!";
        }
        ((TextView) findViewById(R.id.TFbalance)).setText(balanceText);
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following method
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        EditText person1Entered = (EditText) dialog.getDialog().findViewById(R.id.ETperson1Name);
        EditText person2Entered = (EditText) dialog.getDialog().findViewById(R.id.ETperson2Name);
        person1Name = person1Entered.getText().toString();
        person2Name = person2Entered.getText().toString();
        setButtonNames();
        namesSet = true;

        // Saving names
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("person1Name", person1Name);
        editor.putString("person2Name", person2Name);
        editor.putBoolean("namesSet", namesSet);
        editor.commit();
    }


    /**
     * Implementing the log using a ListView
     */
    // Data to show
    List<Map<String, String>> expenseList = new ArrayList<Map<String, String>>();

    // Populating the expenseList
    private void initializeExpenseList() {
        expenseList.clear();
        ArrayList<Expense> expenseObjectList = Expense.getAllExpenses();
        for (Expense expense : expenseObjectList) {
            expenseList.add(0, createExpense("expense", expense.toString()));
        }
    }

    private HashMap<String, String> createExpense(String key, String name) {
        HashMap<String, String> expense = new HashMap<String, String>();
        expense.put(key, name);
        return expense;
    }

    // The data to show
    List<Map<String, String>> planetsList = new ArrayList<Map<String, String>>();

    private void initList() {
        // We populate the planets

        planetsList.add(createPlanet("planet", "Mercury"));
        planetsList.add(createPlanet("planet", "Venus"));
        planetsList.add(createPlanet("planet", "Mars"));
        planetsList.add(createPlanet("planet", "Jupiter"));
        planetsList.add(createPlanet("planet", "Saturn"));
        planetsList.add(createPlanet("planet", "Uranus"));
        planetsList.add(createPlanet("planet", "Neptune"));
        planetsList.add(createPlanet("planet", "Pluto"));
        planetsList.add(createPlanet("planet", "Charon"));
        planetsList.add(createPlanet("planet", "Eris"));
        planetsList.add(createPlanet("planet", "Sedna"));


    }

    private HashMap<String, String> createPlanet(String key, String name) {
        HashMap<String, String> planet = new HashMap<String, String>();
        planet.put(key, name);

        return planet;
    }


}
