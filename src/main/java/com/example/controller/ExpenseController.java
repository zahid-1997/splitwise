package com.example.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.DTO.ExpenseDTO;
import com.example.DTO.ExpenseResponseDTO;
import com.example.DTO.SettleExpenseRequestDTO;
import com.example.DTO.TotalExpenseResponseDTO;
import com.example.helper.ValidationHelper;
import com.example.service.ExpenseService;
import com.example.service.SettleExpenseService;

@RestController
public class ExpenseController {
	
	@Autowired
	ExpenseService expenseService;
	
	@Autowired
	ValidationHelper validationHelper;
	
	@Autowired
	SettleExpenseService settleExpenseService;
	
	@PostMapping("/addExpense")
	public ResponseEntity<ExpenseResponseDTO> addExpense(@RequestBody ExpenseDTO expenseDTO){
		ExpenseResponseDTO expenseResponseDTO = new ExpenseResponseDTO();
		try {
			if (expenseDTO != null) {
				validationHelper.validateExpenseRequest(expenseDTO, expenseResponseDTO);
				expenseResponseDTO = expenseService.addExpense(expenseDTO, expenseResponseDTO);
			}

		}catch(Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.ok().body(expenseResponseDTO);
		}
		
		return ResponseEntity.ok().body(expenseResponseDTO);
	}
	
	@PostMapping("/settleExpense")
	public ResponseEntity<String> settleExpense(@RequestBody SettleExpenseRequestDTO settleExpenseRequestDTO){
		
		try {
			settleExpenseService.settleExpense(settleExpenseRequestDTO);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.ok().body(" Opps! Something went wrong ");
		}
		
		return ResponseEntity.ok().body("Expense settled successfully");
	}
	
	@GetMapping("/getMyExpenses/{email}")
	public ResponseEntity<Map<Integer, List<TotalExpenseResponseDTO>>> getMyExpenses(@PathVariable String email){
		ResponseEntity<Map<Integer, List<TotalExpenseResponseDTO>>> responseEntity;
		try {
			Map<Integer, List<TotalExpenseResponseDTO>> responseList = expenseService.getExpense(email, true);
			responseEntity = new ResponseEntity<>(responseList, HttpStatus.OK);
			return responseEntity;
		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return responseEntity;
		}
	}
	
	@GetMapping("/getOthersExpense/{email}")
	public ResponseEntity<Map<Integer, List<TotalExpenseResponseDTO>>> getOthersExpense(@PathVariable String email){
		ResponseEntity<Map<Integer, List<TotalExpenseResponseDTO>>> responseEntity;
		try {
			Map<Integer, List<TotalExpenseResponseDTO>> responseList = expenseService.getExpense(email, false);
			responseEntity = new ResponseEntity<>(responseList, HttpStatus.OK);
			return responseEntity;
		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return responseEntity;
		}
	}

}
