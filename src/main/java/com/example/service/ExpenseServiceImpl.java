package com.example.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DTO.ExpenseDTO;
import com.example.DTO.ExpenseResponseDTO;
import com.example.DTO.TotalExpenseResponseDTO;
import com.example.entity.Expense;
import com.example.entity.Settlement;
import com.example.entity.Split;
import com.example.entity.User;
import com.example.repo.ExpenseRepositoty;
import com.example.repo.SettlementRepository;
import com.example.repo.UserRepository;
import com.example.split.Constants;
import com.example.split.SplitEnum;

@Service
public class ExpenseServiceImpl implements ExpenseService{

	final double percentage = 100.0;
	@Autowired
	UserRepository userRepository;

	@Autowired
	ExpenseRepositoty expenseRepositoty;
	
	@Autowired
	SettlementRepository settlementRepository;

	public ExpenseResponseDTO addExpense(ExpenseDTO expenseDTO, ExpenseResponseDTO expenseResponse) throws Exception {
		Expense expense = new Expense();
		populateExpense(expense, expenseDTO);
		switch (expenseDTO.getSplitType()) {
		case EQUAL:
			populateSplitAmount(expense, expenseDTO);
			break;

		case UNEQUAL:
			populateUnequalSplitAmount(expense, expenseDTO);
			break;

		case PERCENTAGE:
			populateSplitAmount(expense, expenseDTO);
			break;

		default:
			throw new Exception("Not Valid SplitType");
		}
		expenseRepositoty.save(expense);
		expenseResponse.setFromUser(expenseDTO.getExpenseBy());
		expenseResponse.setDateOfExpense(expense.getDateOfExpense());
		expenseResponse.setTotalAmount(expense.getTotalAmount());
		setUserDetailsMap(expenseResponse, expense);
		populateSettlementObject(expense);
		return expenseResponse;
	}

	private void populateSettlementObject(Expense expense) {
		
		int splitOwnerId = userRepository.getUserIdFromEmail(expense.getExpenseBy());
		
		for (Split split : expense.getSplit()) {
			if (split.getUser().getId() != splitOwnerId) {
				Settlement settleExpense = new Settlement();
				User user = split.getUser();
				List<Settlement> settlements = settlementRepository.getAllSettlementsOfUser(splitOwnerId, user.getId());
				if (settlements.size() > 0) {
					calculateSettlement(split, settleExpense, settlements);
				} else {
					settleExpense.setBalance(split.getSplitAmount());
					settleExpense.setAmountSettled(0.0);
					settleExpense.setIsSettled(Constants.NOT_SETTLED);
				}

				settleExpense.setDateOfSettlement(new Date());
				settleExpense.setExpense(expense);
				settleExpense.setUser(user);
				settleExpense.setExpenseOwnerId(splitOwnerId);
				settlementRepository.save(settleExpense);
			}
		}

	}

	private void calculateSettlement(Split split, Settlement settleExpense, List<Settlement> settlements) {
		Settlement settlementDbObj = settlements.get(0);
		if (settlementDbObj.getBalance() >= split.getSplitAmount()) {
			if (settlementDbObj.getBalance() > split.getSplitAmount()) {
				double balance = settlementDbObj.getBalance() - split.getSplitAmount();
				settlementDbObj.setBalance(balance);
				settlementDbObj.setIsSettled(Constants.PARTIALLY_SETTLED);
				settlementDbObj.setAmountSettled(settlementDbObj.getAmountSettled() + split.getSplitAmount());
			} else {
				settlementDbObj.setIsSettled(Constants.COMPLETELY_SETTLED);
				settlementDbObj.setAmountSettled(settlementDbObj.getBalance() + settlementDbObj.getAmountSettled());
				settlementDbObj.setBalance(0.0);
			}
			settleExpense.setAmountSettled(split.getSplitAmount());
			settleExpense.setBalance(0.0);
			settleExpense.setIsSettled(Constants.COMPLETELY_SETTLED);
			settlementRepository.save(settlementDbObj);
		} else {
			int settlement = 0;
			double amount = split.getSplitAmount();
			while (settlement < settlements.size()) {
				Settlement settlementTempDbObj = settlements.get(settlement);
				amount -= settlementTempDbObj.getBalance();
				if (amount > 0) {
					settlementTempDbObj.setAmountSettled(
							settlementTempDbObj.getAmountSettled() + settlementTempDbObj.getBalance());
					settlementTempDbObj.setBalance(0.0);
					settlementTempDbObj.setDateOfSettlement(new Date());
					settlementTempDbObj.setIsSettled(Constants.COMPLETELY_SETTLED);
					settlementRepository.save(settlementTempDbObj);
					settlement++;
				}
			}
			settleExpense.setIsSettled(Constants.PARTIALLY_SETTLED);
			settleExpense.setBalance(amount);
			settleExpense.setAmountSettled(split.getSplitAmount() - amount);
		}
	}

	private void setUserDetailsMap(ExpenseResponseDTO expenseResponse, Expense expense) {
		Map<String, Map<String, String>> userDetailsMap = new HashMap<>();
		List<Split> splitDetails = expense.getSplit();
		int splitOwnerId = userRepository.getUserIdFromEmail(expense.getExpenseBy());
		for (Split split : splitDetails) {
			if(splitOwnerId != split.getUser().getId()) {
				Map<String, String> userMap = new LinkedHashMap<>();
				User user = split.getUser();
				userMap.put("user", user.getFirstName() + " " + user.getLastName());
				userMap.put("splitAmount", split.getSplitAmount().toString());
				userMap.put("splitType", split.getSplitType().toLowerCase());
				userDetailsMap.put(user.getEmailAddress(), userMap);
			}
		}
		expenseResponse.setExpenseDetails(userDetailsMap);
	}

	private void populateUnequalSplitAmount(Expense expense, ExpenseDTO expenseDTO) {
		List<Split> splitAmountList = new ArrayList<>();
		Map<String, Double> amountMap = expenseDTO.getAmountAgainstUser();
		double totalAmount = expenseDTO.getAmountAgainstUser().values().stream().reduce((a, b) -> a + b).orElse(0.0);
		expense.setTotalAmount(totalAmount);
		for (String user : amountMap.keySet()) {
			Split split = new Split();
			split.setSplitType(expenseDTO.getSplitType().toString());
			split.setSplitAmount(amountMap.get(user));
			populateUser(split, user);
			splitAmountList.add(split);
		}
		expense.setSplit(splitAmountList);

	}

	private void populateSplitAmount(Expense expense, ExpenseDTO expenseDTO) {
		List<Split> splitAmountList = new ArrayList<>();
		Map<String, Double> amountMap = expenseDTO.getAmountAgainstUser();
		double totalAmount = expense.getTotalAmount();
		double splitPercentage = 0.0;
		if (expenseDTO.getSplitType() == SplitEnum.EQUAL) {
			splitPercentage = (100.0 / amountMap.size());
		}

		for (String user : amountMap.keySet()) {
			Split split = new Split();
			double splitAmount = amountMap.get(user) == 0 ? ((splitPercentage * totalAmount) / percentage)
					: ((amountMap.get(user) * totalAmount) / percentage);
			split.setSplitAmount(splitAmount);
			split.setSplitType(expenseDTO.getSplitType().toString());
			populateUser(split, user);
			splitAmountList.add(split);
		}

		expense.setSplit(splitAmountList);

	}

	private void populateUser(Split split, String email) {
		User user = userRepository.findUserByEmailAddress(email);
		split.setUser(user);
	}

	private void populateExpense(Expense expense, ExpenseDTO expenseDTO) {
		expense.setExpenseBy(expenseDTO.getEmailId());
		expense.setExpenseName(expenseDTO.getExpenseName());
		expense.setTotalAmount(expenseDTO.getTotalAmount());
		expense.setDateOfExpense(new Date());
	}

	@Override
	public Map<Integer, List<TotalExpenseResponseDTO>> getExpense(String email, boolean getExpenseFlag) {
		int userId = userRepository.getUserIdFromEmail(email);
		List<Settlement> settlementList;
		if(getExpenseFlag) {
			settlementList = settlementRepository.getAllSettlementsOfUser(userId);
		}else {
			settlementList = settlementRepository.getAllSettlementsByUser(userId);
		} 
		Map<Integer, List<TotalExpenseResponseDTO>> listOfSettlementsMap = new HashMap<>();
		for(Settlement settlement: settlementList) {
			populateTotalExpenseResponseDTO(getExpenseFlag, listOfSettlementsMap, settlement);
		}
		
		return listOfSettlementsMap;
	}

	private void populateTotalExpenseResponseDTO(boolean getExpenseFlag,
			Map<Integer, List<TotalExpenseResponseDTO>> listOfSettlementsMap, Settlement settlement) {
		Expense expense = expenseRepositoty.findById(settlement.getExpense().getId()).orElse(null);
		User user = userRepository.findById(settlement.getUser().getId()).orElse(null);
		if(expense != null && user !=null) {
			TotalExpenseResponseDTO totalExpenseResponseDTO = new TotalExpenseResponseDTO();
			totalExpenseResponseDTO.setExpenseName(expense.getExpenseName());
			populateExpenseWith(getExpenseFlag, settlement, user, totalExpenseResponseDTO);
			totalExpenseResponseDTO.setAmount(settlement.getAmountSettled());
			totalExpenseResponseDTO.setBalance(settlement.getBalance());
			populateSettlementStatus(settlement, totalExpenseResponseDTO);
			if(listOfSettlementsMap.containsKey(expense.getId())) {
				listOfSettlementsMap.get(expense.getId()).add(totalExpenseResponseDTO);
			}else {
				List<TotalExpenseResponseDTO> expenseList = new ArrayList<>();
				expenseList.add(totalExpenseResponseDTO);
				listOfSettlementsMap.put(expense.getId(), expenseList);
			}
		}
	}

	private void populateExpenseWith(boolean getExpenseFlag, Settlement settlement, User user,
			TotalExpenseResponseDTO totalExpenseResponseDTO) {
		if(getExpenseFlag) {
			totalExpenseResponseDTO.setExpenseWith(user.getFirstName() + " " + user.getLastName());
		}else {
			User owner = userRepository.findById(settlement.getExpenseOwnerId()).orElse(null);
			if(owner !=null)
				totalExpenseResponseDTO.setExpenseWith(owner.getFirstName() + " " + owner.getLastName());
		}
	}

	private void populateSettlementStatus(Settlement settlement, TotalExpenseResponseDTO totalExpenseResponseDTO) {
		if(settlement.getIsSettled().equals("2")) {
			totalExpenseResponseDTO.setDate(settlement.getDateOfSettlement());
			totalExpenseResponseDTO.setIsSettled(Constants.STATUS_2);
		}else if(settlement.getIsSettled().equals("1")) {
			totalExpenseResponseDTO.setDate(settlement.getDateOfSettlement());
			totalExpenseResponseDTO.setIsSettled(Constants.STATUS_1);
		}else {
			totalExpenseResponseDTO.setIsSettled(Constants.STATUS_0);
		}
	}

}
