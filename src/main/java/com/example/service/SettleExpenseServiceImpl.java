package com.example.service;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DTO.SettleExpenseRequestDTO;
import com.example.entity.Expense;
import com.example.entity.Settlement;
import com.example.entity.User;
import com.example.repo.ExpenseRepositoty;
import com.example.repo.SettlementRepository;
import com.example.repo.UserRepository;
import com.example.split.Constants;

@Service
public class SettleExpenseServiceImpl implements SettleExpenseService{

	@Autowired
	ExpenseRepositoty expenseRepositoty;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	SettlementRepository settlementRepository;
	
	@Override
	public void settleExpense(SettleExpenseRequestDTO settleExpenseRequestDTO) throws Exception {
		
		Expense expense = expenseRepositoty.findByExpenseUiId(settleExpenseRequestDTO.getExpenseUuid());
		User user = userRepository.findUserByEmailAddress(settleExpenseRequestDTO.getEmail());
		
		if(user == null) {
			throw new Exception(" User Not Present ");
		}
		
		if(expense == null) {
			throw new Exception(" Expense Not Present ");
		}
		
		Settlement settleExpense = settlementRepository.getSettlementByExpenseIdAndUserId(expense.getId(), user.getId());
		
		double balance = calculateBalance(settleExpense.getBalance(), settleExpenseRequestDTO.getAmountToSettle());
		
		if(balance == -1) {
			throw new Exception(" Cannot Settle as totalAmount is less than the Amount provided for settlement ");
		}
		
		double amountSettled = settleExpense.getAmountSettled() + settleExpenseRequestDTO.getAmountToSettle();
		settleExpense.setAmountSettled(amountSettled);
		settleExpense.setBalance(balance);
		settleExpense.setDateOfSettlement(new Date());
		if(balance > 0.0) {
			settleExpense.setIsSettled(Constants.PARTIALLY_SETTLED);
		}else {
			settleExpense.setIsSettled(Constants.COMPLETELY_SETTLED);
		}
		
		settleExpense.setExpense(expense);
		settleExpense.setUser(user);
		settlementRepository.save(settleExpense);
		
	}

	private double calculateBalance(Double totalAmount, Double amountToSettle) {
		if(totalAmount >= amountToSettle) {
			return totalAmount - amountToSettle;
		}
		return -1.0;
	}

	@Override
	public String getExpenseOfUser(String email) {
		
		int userId = userRepository.getUserIdFromEmail(email);
		
		//List<Settlement> settlementList = 
		
		return null;
	}

}
