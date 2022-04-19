package com.example.DTO;

import java.util.HashMap;
import java.util.Map;

import com.example.split.SplitEnum;

public class ExpenseDTO {

	private String expenseName;
	private Double totalAmount;
	private String expenseBy;
	private String emailId;
	private SplitEnum splitType;
	
	
	private Map<String, Double> amountAgainstUser = new HashMap<>();
	
	
	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public SplitEnum getSplitType() {
		return splitType;
	}

	public void setSplitType(SplitEnum splitType) {
		this.splitType = splitType;
	}

	public Map<String, Double> getAmountAgainstUser() {
		return amountAgainstUser;
	}

	public void setAmountAgainstUser(Map<String, Double> amountAgainstUser) {
		this.amountAgainstUser = amountAgainstUser;
	}


	public String getExpenseName() {
		return expenseName;
	}

	public void setExpenseName(String expenseName) {
		this.expenseName = expenseName;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getExpenseBy() {
		return expenseBy;
	}

	public void setExpenseBy(String expenseBy) {
		this.expenseBy = expenseBy;
	}

}
