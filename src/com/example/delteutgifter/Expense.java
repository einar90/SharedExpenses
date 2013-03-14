package com.example.delteutgifter;

import android.text.format.DateFormat;

import java.util.ArrayList;
import java.util.Calendar;

public class Expense {

    private static ArrayList<Expense> expenses = new ArrayList<Expense>();
    private String name;
    private float amount;
    private Calendar timestamp;
    private String description = "";

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Expense(String name, float amount, Calendar timestamp) {
        this.name = name;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public static ArrayList<Expense> getAllExpenses() {
        return expenses;
    }

    public static void addExpense(Expense expense) {
        expenses.add(0, expense);
    }

    public static void deleteExpense(Expense expense) {
        expenses.remove(expense);
    }

    public static void deleteExpense(int index) {
        expenses.remove(index);
    }

    public static void editExpense(Expense expenseToReplace, float newAmount, String newDescription) {
        expenseToReplace.amount = newAmount;
        if (newDescription != null && newDescription.length() > 0) {
            expenseToReplace.description = newDescription;
        }
    }

    public static String serializeExpenses() {
        String serializedExpenses = "";

        for (Expense expense : expenses) {
            serializedExpenses += "#";
            serializedExpenses += expense.getName();
            serializedExpenses += "*" + expense.getAmount();
            serializedExpenses += "*" + expense.getDescription();
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

                String description = remainingString.substring(0, remainingString.indexOf("*"));
                remainingString = remainingString.substring(remainingString.indexOf("*") + 1);

                Calendar timestamp = Calendar.getInstance();
                if (remainingString.indexOf("#") >= 0) {
                    timestamp.setTimeInMillis(Long.parseLong(remainingString.substring(0, remainingString.indexOf("#"))));
                    remainingString = remainingString.substring(remainingString.indexOf("#"));
                } else {
                    timestamp.setTimeInMillis(Long.parseLong(remainingString));
                    remainingString = "";
                }
                Expense expense = new Expense(name, amount, timestamp);
                expense.setDescription(description);
                expenses.add(expense);

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
        return Float.toString(amount) + ",-";
    }

    public String getDateString() {
        String timestampString = DateFormat.format("dd", timestamp).toString();
        return timestampString;
    }

    public String getMonthString() {
        String timestampString = DateFormat.format("MMM", timestamp).toString();
        return timestampString;
    }

    public String getTimestampString() {
        String timestampString = DateFormat.format("dd. MMM - kk:mm", timestamp).toString();
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
