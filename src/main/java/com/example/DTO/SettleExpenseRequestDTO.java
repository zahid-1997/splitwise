package com.example.DTO;

import java.util.Date;

public class SettleExpenseRequestDTO {

	Double amountToSettle;
	String expenseUuid;
	String email;

	public Double getAmountToSettle() {
		return amountToSettle;
	}

	public void setAmountToSettle(Double amountToSettle) {
		this.amountToSettle = amountToSettle;
	}

	public String getExpenseUuid() {
		return expenseUuid;
	}

	public void setExpenseUuid(String expenseUuid) {
		this.expenseUuid = expenseUuid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
