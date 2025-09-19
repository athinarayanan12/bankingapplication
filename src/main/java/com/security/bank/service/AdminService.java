package com.security.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.security.bank.dto.AdminDto;
import com.security.bank.entity.Account;
import com.security.bank.entity.AccountType;
import com.security.bank.entity.BranchType;
import com.security.bank.entity.Role;
import com.security.bank.entity.User;
import com.security.bank.repository.AccountRepository;
import com.security.bank.repository.UserRepository;

@Service
public class AdminService {
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	public void register(AdminDto adminDto) {
		User user =new User();
		user.setName(adminDto.getName());
		user.setUsername(adminDto.getUsername());
		user.setPassword(passwordEncoder.encode(adminDto.getPassword()));
		user.setAddress(adminDto.getAddress());
		user.setNumber(adminDto.getNumber());
		user.setIdentityProof(adminDto.getIdentityProof());
//		Role role =Role.builder().roleName("ROLE _ ADMIN").build();
		Role role =Role.builder().roleName("ROLE_ADMIN").build();
		user.setRoles(role);
		userRepository.save(user);
	}
	public List<User> getAllUser() {
		return userRepository.findAll();
	}
	public User getUserByName(String username) {
//		return userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("Username not found"));
		 return userRepository.findByUsername(username).get();                      // ADDED
	}
	public String deleteUser(Long userId) {
		 if(userRepository.existsById(userId)){
	            userRepository.deleteById(userId);
	            return "Deleted Successfully";
	        }
	         return "Error in deletion";

	}
	public String deactivateAccount(Long userId, Long accountId) {
		if(userRepository.existsById(userId) && accountRepository.existsById(accountId)){
	        User user = userRepository.findById(userId).get();
	        Account account = accountRepository.findById(accountId).get();
	        if(user.getAccountList().contains(account)){
	            System.out.println("Account Found");
	            account.setStatus("INACTIVE");
	            accountRepository.save(account);
	        }
	        return "Deactivated Account for User with id: "+userId;
	    }
	    return "ERROR";
	}
	public String activateAccount(Long userId, Long accountId) {
	   if(userRepository.existsById(userId) && accountRepository.existsById(accountId)){
            User user = userRepository.findById(userId).get();
            Account account = accountRepository.findById(accountId).get();
            if(user.getAccountList().contains(account) && account.getStatus().equals("INACTIVE")){
                System.out.println("1 Account Found");
                account.setStatus("ACTIVE");
                accountRepository.save(account);
                return "Activated Account for User with id: "+userId;
            }
        }
        return "ERROR";
	}
	public List<Account> getActiveAccountsList() {
		return accountRepository.findAllActiveAccounts();
	}
	public List<Account> getInActiveAccountsList() {
		return accountRepository.findAllInActiveAccounts();
	}
	public List<Account> getByAccountType(AccountType accountType) {
		return accountRepository.findAllByAccountType(accountType);
	}
	public List<Account> getByBranchType(BranchType branchType) {
		return accountRepository.findAllByBranch(branchType);
	}

}
