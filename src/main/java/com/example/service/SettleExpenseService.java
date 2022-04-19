package com.example.service;

import com.example.DTO.SettleExpenseRequestDTO;

public interface SettleExpenseService {

	public void settleExpense(SettleExpenseRequestDTO settleExpenseRequestDTO) throws Exception;

	public String getExpenseOfUser(String email);
}
