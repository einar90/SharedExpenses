package com.example.delteutgifter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

public class Expense {

	private static ArrayList<Expense> expenses = new ArrayList<Expense>();

	private String name;
	private float amount;
	private Date timestamp;
	
	public Expense(String name, float amount, Date timestamp) {
		this.name = name;
		this.amount = amount;
		this.timestamp = timestamp;
	}
	
	
	public static ArrayList<Expense> getAllExpenses() {
		return expenses;
	}
	
	public String getName() {
		return name;
	}


	public float getAmount() {
		return amount;
	}


	public Date getTimestamp() {
		return timestamp;
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
			serializedExpenses += "*" + expense.getTimestamp().getTime();
		}
		
		return serializedExpenses;
	}
	
	public static void deserializeExpenses(String serializedExpenses) {
		String remainingString = serializedExpenses;
		
		while (remainingString.length() > 0) {
			String name = remainingString.substring(1, remainingString.indexOf("*"));
			remainingString = remainingString.substring(remainingString.indexOf("*") + 1);
			
			float amount = Float.parseFloat(remainingString.substring(0, remainingString.indexOf("*")));
			remainingString = remainingString.substring(remainingString.indexOf("*") + 1);
			
			Date timestamp = new Date();
			if (remainingString.indexOf("#") >= 0) {
				timestamp = new Date(Long.parseLong(remainingString.substring(0, remainingString.indexOf("#"))));
				remainingString = remainingString.substring(remainingString.indexOf("#"));
			}
			else {
				timestamp = new Date(Long.parseLong(remainingString));
				remainingString = "";
			}
            addExpense(new Expense(name, amount, timestamp));
			
		}
	}
	
	public String toString() {
		return timestamp.toString() + "\t" + name + "\t" + amount;
	}
	
	

}
