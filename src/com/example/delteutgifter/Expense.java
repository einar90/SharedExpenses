package com.example.delteutgifter;

import android.text.format.DateFormat;

import java.util.ArrayList;
import java.util.Calendar;

public class Expense {

    private static ArrayList<Expense> expenses = new ArrayList<Expense>();
    private String name;
    private float amount;
    private Calendar timestamp;

    public Expense(String name, float amount, Calendar timestamp) {
        this.name = name;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public static ArrayList<Expense> getAllExpenses() {
        return expenses;
    }

    public static void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public static void removeLatest() {
        expenses.remove(expenses.size() - 1);
    }

    public static void deleteExpense(Expense expense) {
        expenses.remove(expense);
    }

    public static void deleteExpense(int index) {
        expenses.remove(index);
    }

    public static void editExpense(Expense expenseToReplace,
                                   String newName, float newAmount) {
        expenseToReplace.name = newName;
        expenseToReplace.amount = newAmount;
    }

    public static String serializeExpenses() {
        String serializedExpenses = "";

        for (Expense expense : expenses) {
            serializedExpenses += "#";
            serializedExpenses += expense.getName();
            serializedExpenses += "*" + expense.getAmount();
            serializedExpenses += "*" + expense.getTimestamp().getTimeInMillis();
        }

        return serializedExpenses;
    }

    public static void deserializeExpenses(String serializedExpenses) {
        if (!(expenses.size() > 0)) {
            String remainingString = serializedExpenses;
            while (remainingString.length() > 0) {
                String name = remainingString.substring(1, remainingString.indexOf("*"));
                remainingString = remainingString.substring(remainingString.indexOf("*") + 1);

                float amount = Float.parseFloat(remainingString.substring(0, remainingString.indexOf("*")));
                remainingString = remainingString.substring(remainingString.indexOf("*") + 1);

                Calendar timestamp = Calendar.getInstance();
                if (remainingString.indexOf("#") >= 0) {
                    timestamp.setTimeInMillis(Long.parseLong(remainingString.substring(0, remainingString.indexOf("#"))));
                    remainingString = remainingString.substring(remainingString.indexOf("#"));
                } else {
                    timestamp.setTimeInMillis(Long.parseLong(remainingString));
                    remainingString = "";
                }
                addExpense(new Expense(name, amount, timestamp));

            }
        }
    }

    public String getName() {
        return name;
    }

    public float getAmount() {
        return amount;
    }

    public String getAmountString() {
        return Float.toString(amount);
    }

    public String getDateString() {
        String timestampString = DateFormat.format("dd.\nMMM", timestamp).toString();
        return timestampString;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public String toString() {
        String timestampString = DateFormat.format("dd. MMM (kk:mm)", timestamp).toString();
        return timestampString + ":\t\t" + name + " paid " + amount + ",-";
    }


}
