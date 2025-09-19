package com.security.bank.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.security.bank.entity.Account;
import com.security.bank.entity.AccountType;
import com.security.bank.entity.BranchType;

public interface AccountRepository extends JpaRepository<Account, Long> {

	Optional<Account> findByAccountNumber(Long accountNumber);

	@Query("SELECT a FROM Account a WHERE a.status='ACTIVE'")
	List<Account> findAllActiveAccounts();

	@Query("SELECT a FROM Account a WHERE a.status='INACTIVE'")
	List<Account> findAllInActiveAccounts();
	
//	@Query("SELECT a FROM Account a WHERE a.accountType=:accountType")
	List<Account> findAllByAccountType(@Param("accountType") AccountType accountType);
	
//	@Query("SELECT a FROM Account a WHERE a.branchType=:branchType")
    @Query("SELECT a FROM Account a WHERE a.branch = :branchType")     // 		ADDED
	List<Account> findAllByBranch(@Param("branchType") BranchType branchType);
}
