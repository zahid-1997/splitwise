package com.example.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.Expense;
import com.example.entity.User;

@Repository
public interface ExpenseRepositoty extends JpaRepository<Expense, Integer>{
	
	public Expense findByExpenseUiId(String expenseUiId);
	
}
