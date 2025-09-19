package com.security.bank.entity;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Account {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private AccountType accountType;
	private String status;
	private double balance;
	private float interestRate;
	@Enumerated(EnumType.STRING)
	private BranchType branch;
	private String proof;
	private Date openingDate;
	private Long accountNumber;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Nominee nominee;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Card card;
	
	@ManyToOne
	@JsonIgnoreProperties("accountList")
	private User user;

}
