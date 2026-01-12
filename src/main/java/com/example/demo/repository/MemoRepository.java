package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.MemoEntity;

/**
 * `MemoEntity` のデータベース操作。
 */
public interface MemoRepository extends JpaRepository<MemoEntity, Long>{
    
    Optional<MemoEntity> findByMemoDate(LocalDate memoDate);

	List<MemoEntity> findByMemoDateBetween(LocalDate start, LocalDate end);

}