package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.MemoEntity;
import com.example.demo.repository.MemoRepository;

import jakarta.transaction.Transactional;

@Service
public class MemoService {

	@Autowired
    private MemoRepository memoRepository;
	
	// カレンダーに表示させるメモ有無
	public List<MemoEntity> existMemo(int year, int month) {
		LocalDate start = LocalDate.of(year, month, 1);
		LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
		return memoRepository.findByMemoDateBetween(start, end);
	}
	
	// メモデータがあるか確認
	public Optional<MemoEntity> findMemo(LocalDate date) {
		return memoRepository.findByMemoDate(date);
	}
	
	// メモ新規登録 更新
	@Transactional
	public void saveOrUpdateMemo(MemoEntity memo) {
		memoRepository.save(memo);
    }
	
	// メモ削除
	@Transactional
	public void deleteByDate(LocalDate memoDate) {
		memoRepository.findByMemoDate(memoDate)
			.ifPresent(memoRepository::delete);
	}
}
