package com.example.wordapi.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class QuizResult {
    @Id @GeneratedValue
    private Long id;

    private Long quizId;
    private String userChoice;
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQuizId() {
		return quizId;
	}

	public void setQuizId(Long quizId) {
		this.quizId = quizId;
	}

	public String getUserChoice() {
		return userChoice;
	}

	public void setUserChoice(String userChoice) {
		this.userChoice = userChoice;
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	private boolean correct;

    private LocalDateTime timestamp;

    public QuizResult() {
        this.timestamp = LocalDateTime.now();
    }

    public QuizResult(Long quizId, String userChoice, boolean correct) {
        this.quizId = quizId;
        this.userChoice = userChoice;
        this.correct = correct;
        this.timestamp = LocalDateTime.now();
    }

}
