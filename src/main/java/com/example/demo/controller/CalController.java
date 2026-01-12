package com.example.demo.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.CalModel;
import com.example.demo.service.CalService;

@Controller
public class CalController {

	private final CalService calService;

	public CalController(CalService calService) {
		this.calService = calService;
	}

	@GetMapping("/calendar")
	public String showCalendar(
			@RequestParam(required = false) Integer year,
			@RequestParam(required = false) Integer month,
			Model model) {

		CalModel calDate;

		// 引数なし
		if (year == null || month == null) {
			calDate = calService.getcalDate();
		}
		// 引数あり 前月次月のカレンダー表示
		else {
			calDate = calService.getcalDate(year, month);
		}

		// calendar.html に情報を渡す
		model.addAttribute("thisYear", calDate.getYear());
		model.addAttribute("thisMonth", calDate.getMonth());

		// 月 英語表記
		Month monthEnum = Month.of(calDate.getMonth());
		String monthEn = monthEnum.getDisplayName(TextStyle.FULL, Locale.ENGLISH).toLowerCase();
		model.addAttribute("monthEn", monthEn);

		model.addAttribute("date", calDate.getDate());
		model.addAttribute("memo", calDate.getMemo());

		// 今日の日付
		LocalDate today = LocalDate.now();

		DayOfWeek dayOfWeek = today.getDayOfWeek();
		String dayOfWeekEn = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH).toLowerCase();

		// 今月 英語表記
		Month todayMonthEnum = today.getMonth();
		String todayMonthEn = todayMonthEnum.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
				.toLowerCase();

		model.addAttribute("currentYear", today.getYear());
		model.addAttribute("currentMonth", today.getMonthValue());
		model.addAttribute("currentDay", String.valueOf(today.getDayOfMonth()));
		model.addAttribute("currentMonthEn", todayMonthEn);
		model.addAttribute("currentWeek", dayOfWeekEn);

		// calendar.html を表示
		return "calendar";
	}
}
