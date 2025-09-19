package com.security.bank.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.security.bank.dto.AccountDto;
import com.security.bank.dto.KycDto;
import com.security.bank.dto.NomineeDto;
import com.security.bank.entity.Account;
import com.security.bank.entity.AccountType;
import com.security.bank.entity.BranchType;
import com.security.bank.entity.Card;
import com.security.bank.entity.CardType;
import com.security.bank.entity.Nominee;
import com.security.bank.entity.User;
import com.security.bank.repository.AccountRepository;
import com.security.bank.repository.CardRepository;
import com.security.bank.repository.NomineeRepository;
import com.security.bank.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	AccountRepository accountRepository;
	
    @Autowired
    CardService cardService;
    @Autowired
    NomineeRepository nomineeRepository;
    @Autowired
    CardRepository cardRepository;
	    
	
	public void createAccount(AccountDto accountDto, Long userId) {
		  User user = new User();
	       if(userRepository.existsById(userId)){
	           user = userRepository.findById(userId).get();
	       }
	       Account account = new Account();
	       Card card = new Card();
	       card.setCardNumber(Long.parseLong(cardService.generateCardNumber()));
	       card.setCvv(cardService.generateCvv());
	       card.setCardHolderName(user.getName());
	       card.setStatus("ACTIVE");
	       card.setPin(1122L);
	       card.setAllocationDate(new Date());
	       
	       Calendar calendar = Calendar.getInstance();
	       calendar.setTime(new Date());
	       calendar.add(Calendar.YEAR, 5);
	       card.setExpiryDate(calendar.getTime());

	       switch(accountDto.getAccountType()){
	           case "SAVINGS":{
	               card.setCardType(CardType.DEBIT_GLOBAL);
	               card.setDailyLimit(40000);
	               cardRepository.save(card);
	               
	               account.setAccountType(AccountType.SAVINGS);
	               account.setInterestRate(2.70F);
	               account.setBranch(BranchType.BOB);
	               account.setCard(card);
	               break;
	           }
	           case "CURRENT":{
	               card.setCardType(CardType.DEBIT_GLOBAL);
	               card.setDailyLimit(50000);
	               cardRepository.save(card);
	               
	               account.setAccountType(AccountType.CURRENT);
	               account.setBranch(BranchType.ICIC);
	               account.setCard(card);
	               account.setInterestRate(5.2F);
	               break;
	           }
	           case "PPF":{
	               account.setAccountType(AccountType.PPF);
	               account.setBranch(BranchType.SBI);
	               account.setInterestRate(7.4F);
	               break;
	           }
	           case "SALARY":{
	               card.setCardType(CardType.CREDIT_MASTER);
	               card.setDailyLimit(75000);
	               cardRepository.save(card);
	               account.setAccountType(AccountType.SALARY);
	               account.setBranch(BranchType.HDFC);
	               account.setCard(card);
	               account.setInterestRate(4.1F);
	               break;
	           }
	           default:{
	               throw  new RuntimeException("No AccountType Selected");
	           }
	       }
	       account.setAccountNumber(generateRandomNumber());
	       account.setStatus("ACTIVE");
	       account.setBalance(accountDto.getBalance());
	       Nominee nominee = nomineeRepository.save(accountDto.getNominee());
	       account.setNominee(nominee);
	       account.setProof(accountDto.getProof());
	       account.setOpeningDate(new Date());
	       account.setUser(user);
	       accountRepository.save(account);
	       log.info("Account created Successfully");
	}
	 public Long generateRandomNumber() {
	        Random random = new Random();
	        // Generate a random number between 10000000 and 99999999 (8-digit number)
	        return 10000000 + random.nextLong(90000000);
		 
		  }
	
	public Account getAccountByNumber(Long accountNumber) {
		Account account = accountRepository.findByAccountNumber(accountNumber).get();
        account.setUser(null);
        return account;
	}
	public List<Account> getAllAccount(Long userId) {
	    User user = userRepository.findById(userId).get();
		return user.getAccountList();
	}

	public Double getAccountBalance(Long accountNumber) {
		  Account account = accountRepository.findByAccountNumber(accountNumber).get();
	        return account.getBalance();
	}

	public Nominee getNominee(Long accountNumber) {
		 Account account = accountRepository.findByAccountNumber(accountNumber).get();
	        return account.getNominee();
	}
	
	public void updateNominee(NomineeDto nomineeDto, Long accountId) {
		  Account fetchedAccount = accountRepository.findById(accountId).get();
	        Nominee newNominee = fetchedAccount.getNominee();
	        newNominee.setName(nomineeDto.getName());
	        newNominee.setAccountNumber(nomineeDto.getAccountNumber());
	        newNominee.setRelation(nomineeDto.getRelation());
	        newNominee.setAge(nomineeDto.getAge());
	        newNominee.setGender(nomineeDto.getGender());
	        accountRepository.save(fetchedAccount);
	}
	
	public User getKycDetails(Long accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).get();

		User user=account.getUser();
		user.setAccountList(null);
		user.setInvestmentList(null);
		return user;
	}
    public void updateKyc(KycDto kycDto,Long accountId) {

	    Account fetchedAccount = accountRepository.findById(accountId).get();
        User fetechedUser = fetchedAccount.getUser();
        if(!kycDto.getName().isEmpty()) fetechedUser.setName(kycDto.getName());
        if(!kycDto.getAddress().isEmpty()) fetechedUser.setAddress(kycDto.getAddress());
        if(kycDto.getNumber()!=null) fetechedUser.setNumber(kycDto.getNumber());
        if(!kycDto.getIdentityProof().isEmpty()) fetechedUser.setIdentityProof(kycDto.getIdentityProof());
        userRepository.save(fetechedUser);
	}
	public Account getAccountSummary(Long accountNumber) {
//		Account account=getAccountByNumber(accountNumber);
        Account account = accountRepository.findByAccountNumber(accountNumber).get();
		account.setUser(null);
		return account;
	}
}
