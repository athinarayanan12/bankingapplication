package com.security.bank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.bank.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long>{

	Optional<Card> findByCardNumber(Long cardNumber);

}
