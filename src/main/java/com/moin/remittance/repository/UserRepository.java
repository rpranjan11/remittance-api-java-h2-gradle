package com.moin.remittance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moin.remittance.model.UserDetail;

public interface UserRepository extends JpaRepository<UserDetail, String> {

}
