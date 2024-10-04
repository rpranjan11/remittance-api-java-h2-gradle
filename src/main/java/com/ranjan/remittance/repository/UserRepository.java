package com.ranjan.remittance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ranjan.remittance.model.UserDetail;

public interface UserRepository extends JpaRepository<UserDetail, String> {

}
