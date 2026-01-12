package com.example.demo.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.MemoEntity;
import com.example.demo.model.CalModel;

@Service
public class CalService {

	@Autowired
	private MemoService memoService;
	
	/**
	 * 今月のカレンダー情報を取得。
	 * 
	 * @return 今月の情報
	 */

	// 引数なし 今月を表示する場合
	public CalModel getcalDate() {
		// 現在日を取得
		LocalDate today = LocalDate.now();

		// getcalDate に今の年月を渡す
		return getcalDate(today.getYear(), today.getMonthValue());
	}

	// 引数あり 前月次月のカレンダー表示
	public CalModel getcalDate(int year, int month) {

		// 月の1日に設定
		LocalDate setday = LocalDate.of(year, month, 1);

		// 年月をint型に変換
		int thisYear = setday.getYear();
		int thisMonth = setday.getMonthValue();

		// 今月の日数をカウント
		int countDate = setday.lengthOfMonth();

		// 今月1日は何曜日か (数値)
		DayOfWeek firstWeek = setday.getDayOfWeek();

		// 月曜が 1 だったら空枠は 1 枠
		int before = firstWeek.getValue();

		// 日曜 7 だった場合は空枠を 0 に
		if (before == 7) {
			before = 0;
		}

		// 月の最終日に設定
		LocalDate lastDate = setday.withDayOfMonth(countDate);

		// 月の最終日は何曜日か（数値）
		DayOfWeek lastWeek = lastDate.getDayOfWeek();

		// 最終日が水曜 3 だった場合は空枠は 3 
		int after = 6 - lastWeek.getValue();

		// 枠の数
		int total = before + countDate + after;

		// 7 列にしたら何行になるか
		int rows = total / 7;

		// 日付の二次元配列 1つ目：行　2つ目：列
		String[][] date = new String[rows][7];

		// メモ用
		boolean[][] memo = new boolean[rows][7];
		
		List<MemoEntity> memoList = memoService.existMemo(year, month);

		// 日付の2次元配列を設定
		// iが行　ｊが列
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < 7; j++) {
				// 行が1で列が空枠数の上限まで　OR　行が最終行で列が空枠
				if (i == 0 && j < before || i == rows - 1 && j >= (7 - after)) {
					// 空枠を入れる
					date[i][j] = "";
					memo[i][j] = false;
				} else {
					// 数字を枠に入れる
					int dateNum = i * 7 + j + 1 - before;
					date[i][j] = String.valueOf(dateNum);
					
					// メモの有無
					LocalDate current = LocalDate.of(year, month, dateNum);
                    memo[i][j] = memoList.stream()
                    		.anyMatch(m -> m.getMemoDate().equals(current));
				}
			}
		}

		// CalModelに情報を渡す
		return new CalModel(thisYear, thisMonth, date, memo);
	}
}
