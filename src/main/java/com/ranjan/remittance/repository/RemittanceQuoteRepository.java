package com.ranjan.remittance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ranjan.remittance.model.RemittanceQuote;

public interface RemittanceQuoteRepository extends JpaRepository<RemittanceQuote, String> {

}
