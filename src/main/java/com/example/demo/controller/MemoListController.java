package com.example.demo.controller;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.MemoEntity;
import com.example.demo.service.MemoService;

@Controller
public class MemoListController {

	@Autowired
	private MemoService memoService;

	@GetMapping("/memoList")
	public String showMemoList(
			@RequestParam int year,
			@RequestParam int month,
			Model model) {

		// パラメータの日付を表示
		model.addAttribute("year", year);
		model.addAttribute("month", month);

		// 月 英語表記
		Month monthEnum = Month.of(month);
		String monthEn = monthEnum.getDisplayName(TextStyle.FULL, Locale.ENGLISH).toLowerCase();
		model.addAttribute("monthEn", monthEn);

		List<MemoEntity> memoList = memoService.existMemo(year, month);

		// 日付順にソート
		memoList.sort(Comparator.comparing(MemoEntity::getMemoDate));

		model.addAttribute("memoList", memoList);

		return "memoList";
	}
}
