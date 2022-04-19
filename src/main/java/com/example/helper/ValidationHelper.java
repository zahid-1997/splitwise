package com.example.helper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.DTO.ExpenseDTO;
import com.example.DTO.ExpenseResponseDTO;
import com.example.entity.User;
import com.example.repo.UserRepository;
import com.example.split.SplitEnum;

@Component
public class ValidationHelper {
	
	@Autowired
	UserRepository userRepository;
	
	public void validateExpenseRequest(ExpenseDTO expenseDTO, ExpenseResponseDTO expenseResponseDTO) throws Exception {
		List<String> emailList = expenseDTO.getAmountAgainstUser().keySet().stream().collect(Collectors.toList());
		expenseResponseDTO.getExpenseDetails().keySet().stream().forEach(s -> System.out.println(s));

		if (userRepository.findUserByEmailAddress(expenseDTO.getEmailId()) == null) {
			expenseResponseDTO.setError("User Not Present");
			throw new Exception("User Not Present");
		}

		List<User> userList = userRepository.getListofUsersByEmail(emailList);

		if (userList.size() != emailList.size()) {
			expenseResponseDTO.setError("One of the user is not registered");
			throw new Exception("One of the user is not registered");
		}
		
		if (expenseDTO.getSplitType().equals(SplitEnum.UNEQUAL)) {
			long count = expenseDTO.getAmountAgainstUser().values().stream().mapToInt(Double::intValue)
					.filter(amount -> amount == 0).count();
			if (count > 0) {
				expenseResponseDTO.setError("Amount cannot be Zero for Unequal Type Split");
				throw new Exception("Amount cannot be Zero for Unequal Type Split");
			}
		}
	}

}
