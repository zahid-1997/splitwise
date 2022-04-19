package com.example.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.Settlement;


@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Integer> {
	//need to change to jpql
	//@Query("from Settlement where Expense.id = :expenseId AND User.id = :userId")
	@Query(value = "SELECT * FROM Settlement s where s.expense_id = :expenseId AND s.user_id = :userId", nativeQuery = true)
	public Settlement getSettlementByExpenseIdAndUserId(@Param("expenseId") int expenseId, @Param("userId") int userId);
	
	@Query(value = "SELECT * FROM Settlement s where s.expense_owner_id=:userId AND s.user_id=:expenseOwnerId AND is_settled <> '2' order by balance desc", nativeQuery = true)
	public List<Settlement> getAllSettlementsOfUser(@Param("expenseOwnerId") int expenseOwnerId, @Param("userId") int userId);
	
	@Query(value = "SELECT * FROM Settlement s where s.expense_owner_id = :userId", nativeQuery = true)
	public List<Settlement> getAllSettlementsOfUser(@Param("userId") int userId);
	
	@Query(value = "SELECT * FROM Settlement s where s.user_id = :userId", nativeQuery = true)
	public List<Settlement> getAllSettlementsByUser(@Param("userId") int userId);
	
}
