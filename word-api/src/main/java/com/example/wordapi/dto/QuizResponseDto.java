package com.example.wordapi.dto;

import java.util.List;

public class QuizResponseDto {
    private Long quizId;
    private String sentence;
    private List<String> choices;

    public QuizResponseDto(Long quizId, String sentence, List<String> choices) {
        this.quizId = quizId;
        this.sentence = sentence;
        this.choices = choices;
    }

    // Getter들
    public Long getQuizId() {
        return quizId;
    }

    public String getSentence() {
        return sentence;
    }

    public List<String> getChoices() {
        return choices;
    }
}
