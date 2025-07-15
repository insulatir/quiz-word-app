package com.example.wordapi.dto;

import java.util.List;

public class QuizDto {
    private Long quizId;
    private String sentence;
    private List<String> choices;

    public QuizDto() {}

    public QuizDto(Long quizId, String sentence, List<String> choices) {
        this.quizId = quizId;
        this.sentence = sentence;
        this.choices = choices;
    }

	public Long getQuizId() {
		return quizId;
	}

	public void setQuizId(Long quizId) {
		this.quizId = quizId;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public List<String> getChoices() {
		return choices;
	}

	public void setChoices(List<String> choices) {
		this.choices = choices;
	}
    
    
}

