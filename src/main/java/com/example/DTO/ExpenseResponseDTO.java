package com.example.DTO;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExpenseResponseDTO {
	
	private String fromUser;
	private Date dateOfExpense;
	private Double totalAmount;
	private Map<String, Map<String, String>> expenseDetails = new HashMap<>();
	private String error;
	
	public String getFromUser() {
		return fromUser;
	}
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}
	public Date getDateOfExpense() {
		return dateOfExpense;
	}
	public void setDateOfExpense(Date dateOfExpense) {
		this.dateOfExpense = dateOfExpense;
	}
	public Map<String, Map<String, String>> getExpenseDetails() {
		return expenseDetails;
	}
	public void setExpenseDetails(Map<String, Map<String, String>> expenseDetails) {
		this.expenseDetails = expenseDetails;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	
	

}

