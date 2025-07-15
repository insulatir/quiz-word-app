package com.example.wordapi.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Quiz {
    @Id @GeneratedValue
    private Long id;
    private String sentence;

    @ElementCollection
    private List<String> choices;

    private String answer;

    public Quiz() {}

    public Quiz(String sentence, List<String> choices, String answer) {
        this.sentence = sentence;
        this.choices = choices;
        this.answer = answer;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
    
    
}

