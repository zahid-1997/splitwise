package com.example.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;

@Entity
public class Expense {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@NotNull
	private int id;

	@NotNull
	@Column(unique = true)
	private String expenseUiId;

	private String expenseName;
	private Double totalAmount;
	private String expenseBy;
	private Date dateOfExpense;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Split> split;

	@PrePersist
	protected void onCreate() {
		setExpenseUiId(java.util.UUID.randomUUID().toString());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<Split> getSplit() {
		return split;
	}

	public void setSplit(List<Split> split) {
		this.split = split;
	}

	public String getExpenseName() {
		return expenseName;
	}

	public void setExpenseName(String expenseName) {
		this.expenseName = expenseName;
	}

	public String getExpenseBy() {
		return expenseBy;
	}

	public void setExpenseBy(String expenseBy) {
		this.expenseBy = expenseBy;
	}

	public Date getDateOfExpense() {
		return dateOfExpense;
	}

	public void setDateOfExpense(Date dateOfExpense) {
		this.dateOfExpense = dateOfExpense;
	}

	public String getExpenseUiId() {
		return expenseUiId;
	}

	public void setExpenseUiId(String expenseUiId) {
		this.expenseUiId = expenseUiId;
	}

}
