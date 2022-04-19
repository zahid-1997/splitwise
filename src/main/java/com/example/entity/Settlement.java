package com.example.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Settlement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int id;
	private Double amountSettled;
	private Double balance;
	private String isSettled;
	private Date dateOfSettlement;
	private int expenseOwnerId;

	@ManyToOne(cascade = CascadeType.ALL)
	Expense expense;
	
	@OneToOne
	private
	User user;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Double getAmountSettled() {
		return amountSettled;
	}

	public void setAmountSettled(Double amountSettled) {
		this.amountSettled = amountSettled;
	}

	public String getIsSettled() {
		return isSettled;
	}

	public void setIsSettled(String isSettled) {
		this.isSettled = isSettled;
	}

	public Expense getExpense() {
		return expense;
	}

	public void setExpense(Expense expense) {
		this.expense = expense;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Date getDateOfSettlement() {
		return dateOfSettlement;
	}

	public void setDateOfSettlement(Date dateOfSettlement) {
		this.dateOfSettlement = dateOfSettlement;
	}

	public int getExpenseOwnerId() {
		return expenseOwnerId;
	}

	public void setExpenseOwnerId(int expenseOwnerId) {
		this.expenseOwnerId = expenseOwnerId;
	}
}
