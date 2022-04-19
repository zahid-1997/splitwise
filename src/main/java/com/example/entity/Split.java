package com.example.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Split {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@NotNull
	private int id;

	private Double splitAmount;

	private String splitType;

	@OneToOne(cascade=CascadeType.ALL)
	private User user;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Double getSplitAmount() {
		return splitAmount;
	}

	public void setSplitAmount(Double splitAmount) {
		this.splitAmount = splitAmount;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getSplitType() {
		return splitType;
	}

	public void setSplitType(String splitType) {
		this.splitType = splitType;
	}

}
