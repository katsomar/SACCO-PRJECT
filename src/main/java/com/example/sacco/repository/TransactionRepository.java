package com.example.sacco.repository;

import com.example.sacco.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserUserIdOrderByTimestampDesc(String userId);
}
