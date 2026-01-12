package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * カレンダーのデータを保持
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalModel {
	// 年
	private int year;
	// 月
	private int month;
	// 日
	String[][] date = new String[6][7];

	// メモ
	boolean[][] memo = new boolean[6][7];
}
