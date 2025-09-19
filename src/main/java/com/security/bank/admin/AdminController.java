package com.security.bank.admin;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.security.bank.dto.AdminDto;
import com.security.bank.entity.Account;
import com.security.bank.entity.AccountType;
import com.security.bank.entity.BranchType;
import com.security.bank.entity.User;
import com.security.bank.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	AdminService adminService;
	
	@PostMapping("/add")
	@ResponseStatus(HttpStatus.CREATED)
	public void register(@RequestBody AdminDto adminDto) {
		adminService.register(adminDto);
	}
	@GetMapping("/getAllUser")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> getAllUser(){
		return adminService.getAllUser();
		
	}
	@GetMapping("/getUserByName/{username}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('ADMIN')")
	public User getUserByName(@PathVariable String username){
		return adminService.getUserByName(username);
		
	}
	@DeleteMapping("/deleteUser/{userId}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('ADMIN')")
	public String deleteUser(@PathVariable Long userId) {
		return adminService.deleteUser(userId);
		
	}
	@PutMapping("/account/deactivate")
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PreAuthorize("hasRole('ADMIN')")
	public String deactivateAccount(@RequestParam Long userId, @RequestParam Long accountId) {
		return adminService.deactivateAccount(userId,accountId);
	}
	@PutMapping("/account/activate")
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PreAuthorize("hasRole('ADMIN')")
	public String activateAccount(@RequestParam Long userId, @RequestParam Long accountId) {
		return adminService.activateAccount(userId,accountId);
	}
	@GetMapping("/account/getActiveAccountsList")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('ADMIN')")
	public List<Account> getActiveAccountsList(){
		return adminService.getActiveAccountsList();
		
	}
	@GetMapping("/account/getInActiveAccountsList")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('ADMIN')")
	public List<Account> getInActiveAccountsList(){
		return adminService.getInActiveAccountsList();
		
	}
	@GetMapping("/accountList/ByAccountType/{accType}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('ADMIN')")
	public List<Account> getByAccountType(@PathVariable AccountType accType){
		return adminService.getByAccountType(accType);
		
	}
	@GetMapping("/accountList/ByBranchType/{branchType}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('ADMIN')")
	public List<Account> getByBranchType(@PathVariable BranchType branchType){
		return adminService.getByBranchType(branchType);
		
	}
}
