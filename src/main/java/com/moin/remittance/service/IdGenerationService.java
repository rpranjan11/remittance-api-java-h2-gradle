package com.moin.remittance.service;

import org.springframework.stereotype.Service;

@Service
public class IdGenerationService {
    public Long newAccountId() {
        return System.nanoTime();
    }
}