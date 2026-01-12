package com.example.demo.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.MemoEntity;
import com.example.demo.service.MemoService;

@Controller
public class MemoController {

	@Autowired
	private MemoService memoService;

	@GetMapping("/memo")
	public String showMemo(
			@RequestParam int year,
			@RequestParam int month,
			@RequestParam int date,
			Model model) {

		// パラメータの日付を表示
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("date", date);

		// メモデータ確認用の日付を設定
		LocalDate memoDate = LocalDate.of(year, month, date);

		// 曜日
		DayOfWeek dayOfWeek = memoDate.getDayOfWeek();
		String dayOfWeekEn = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH).toLowerCase();
		model.addAttribute("week", dayOfWeekEn);

		// 月 英語表記
		Month monthEnum = memoDate.getMonth();
		String monthEn = monthEnum.getDisplayName(TextStyle.FULL, Locale.ENGLISH).toLowerCase();
		model.addAttribute("monthEn", monthEn);

		// メモデータ確認
		Optional<MemoEntity> memoOpt = memoService.findMemo(memoDate);

		// メモデータあり 既存のデータを表示
		if (memoOpt.isPresent()) {
			model.addAttribute("memoTitle", memoOpt.get().getTitle());
			model.addAttribute("memoContent", memoOpt.get().getContent());
			model.addAttribute("hasMemo", true);
		}
		// メモデータなし 空のテキストボックスを表示
		else {
			model.addAttribute("memoTitle", "");
			model.addAttribute("memoContent", "");
			model.addAttribute("hasMemo", false);
		}

		// calendar.html を表示
		return "memo";
	}

	// メモ新規登録 更新
	@PostMapping("/saveMemo")
	public String saveMemo(
			@RequestParam int year,
			@RequestParam int month,
			@RequestParam int date,
			@RequestParam String memoTitle,
			@RequestParam String memoContent,
			Model model) {

		// メモデータ用の日付を設定
		LocalDate memoDate = LocalDate.of(year, month, date);

		// メモデータ確認
		Optional<MemoEntity> memoOpt = memoService.findMemo(memoDate);

		// 曜日
		DayOfWeek dayOfWeek = memoDate.getDayOfWeek();
		String dayOfWeekEn = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH).toLowerCase();

		// 入力エラー用
		String titleError = "";
		String contentError = "";

		// タイトル入力チェック
		if (memoTitle.length() == 0) {
			if (memoOpt.isPresent()) {
				titleError = "更新に失敗しました。タイトルを入力してください";
			} else {
				titleError = "タイトルを入力してください";
			}
		} else if (memoTitle.length() > 10) {
			if (memoOpt.isPresent()) {
				titleError = "更新に失敗しました。タイトルは、100桁以内で入力してください";
			} else {
				titleError = "タイトルは、100桁以内で入力してください";
			}
		}

		// メモ内容入力チェック
		if (memoContent.length() == 0) {
			if (memoOpt.isPresent()) {
				contentError = "更新に失敗しました。メモ内容を入力してください";
			} else {
				contentError = "メモ内容を入力してください";
			}
		} else if (memoContent.length() > 100) {
			if (memoOpt.isPresent()) {
				contentError = "更新に失敗しました。メモ内容は、100桁以内で入力してください";
			} else {
				contentError = "メモ内容は、100桁以内で入力してください";
			}
		}

		// エラーがあったら エラー文を表示したメモ画面に戻る
		if (!titleError.isEmpty() || !contentError.isEmpty()) {
			model.addAttribute("year", year);
			model.addAttribute("month", month);
			model.addAttribute("date", date);
			model.addAttribute("week", dayOfWeekEn);

			model.addAttribute("memoTitle", memoTitle);
			model.addAttribute("memoContent", memoContent);

			// タイトルエラー文
			if (!titleError.isEmpty()) {
				model.addAttribute("errorTitle", titleError);
			}
			// メモ内容エラー文
			if (!contentError.isEmpty()) {
				model.addAttribute("errorContent", contentError);
			}

			// メモデータが既存の場合はhasMemoをつける 
			if (memoOpt.isPresent()) {
				model.addAttribute("hasMemo", true);
			}

			// メモ画面に戻る
			return "memo";
		}

		MemoEntity memo = memoOpt.orElseGet(MemoEntity::new);
		memo.setMemoDate(memoDate);
		memo.setTitle(memoTitle);
		memo.setContent(memoContent);

		memoService.saveOrUpdateMemo(memo);

		return "redirect:/calendar?year=" + year + "&month=" + month;
	}

	// メモ削除
	@PostMapping("/deleteMemo")
	public String deleteMemo(
			@RequestParam int year,
			@RequestParam int month,
			@RequestParam int date) {

		// メモデータ用の日付を設定
		LocalDate memoDate = LocalDate.of(year, month, date);

		// メモを削除
		memoService.deleteByDate(memoDate);

		return "redirect:/calendar?year=" + year + "&month=" + month;
	}
}