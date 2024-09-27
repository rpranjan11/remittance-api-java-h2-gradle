package com.moin.remittance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moin.remittance.model.RemittanceQuote;

public interface RemittanceQuoteRepository extends JpaRepository<RemittanceQuote, String> {

}
