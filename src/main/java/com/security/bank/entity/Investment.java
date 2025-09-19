package com.security.bank.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Investment {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@Enumerated(EnumType.STRING)
	private InvestmentType investmentType;
	private String risk;
	private double amount;
	private float returns;
	private String duration;
	private String companyName;
	
	@ManyToOne
//	@JsonIgnore
	 @JsonIgnoreProperties("investmentList")  // ADDED
	private User user;
}
