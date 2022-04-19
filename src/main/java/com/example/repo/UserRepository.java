package com.example.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("from User where emailAddress= :email OR phoneNumber= :phoneNo")
	public User findByEmailAddressAndPhone(@Param("email") String email, @Param("phoneNo") String phoneNo);

	public User findUserByEmailAddress(String email);

	@Query("from User where emailAddress IN (:emailAddress)")
	public List<User> getListofUsersByEmail(@Param("emailAddress") List<String> emailAddress);
	
	@Query(value = "select id from User where email_address = :emailAddress", nativeQuery=true)
	public int getUserIdFromEmail(@Param("emailAddress") String emailAddress);
}
