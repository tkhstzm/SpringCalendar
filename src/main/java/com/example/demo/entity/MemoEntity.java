package com.example.demo.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_calendar_memo")
public class MemoEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// memo_date
	private LocalDate memoDate;
	
	// title
	@Column(length = 225)
	private String title;
	
	// content
	@Column(columnDefinition = "TEXT")
	private String content;
	
	// memo_date getter setter
	public LocalDate getMemoDate() {
        return memoDate;
    }

    public void setMemoDate(LocalDate memoDate) {
        this.memoDate = memoDate;
    }
    
    // title getter setter
    public String getTitle() {
    	return title;
    }
    
    public void setTitle(String title) {
    	this.title = title;
    }
    
    // content getter setter
	public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}