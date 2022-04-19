package com.example.DTO;

import java.util.Date;

public class TotalExpenseResponseDTO {
	
	private String expenseName;
	private Double amount;
	private String expenseWith;
	private String isSettled;
	private Double balance;
	private Date date;
	
	public String getExpenseName() {
		return expenseName;
	}
	public void setExpenseName(String expenseName) {
		this.expenseName = expenseName;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getExpenseWith() {
		return expenseWith;
	}
	public void setExpenseWith(String expenseWith) {
		this.expenseWith = expenseWith;
	}
	public String getIsSettled() {
		return isSettled;
	}
	public void setIsSettled(String isSettled) {
		this.isSettled = isSettled;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
}
