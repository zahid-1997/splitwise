package com.example.service;

import java.util.List;
import java.util.Map;

import com.example.DTO.ExpenseDTO;
import com.example.DTO.ExpenseResponseDTO;
import com.example.DTO.TotalExpenseResponseDTO;

public interface ExpenseService {

	public ExpenseResponseDTO addExpense(ExpenseDTO expenseDTO, ExpenseResponseDTO expenseResponse) throws Exception;
	
	public Map<Integer, List<TotalExpenseResponseDTO>> getExpense(String email, boolean getExpenseFlag);
}
