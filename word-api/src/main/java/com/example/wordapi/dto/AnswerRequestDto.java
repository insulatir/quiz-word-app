package com.example.wordapi.dto;

public class AnswerRequestDto {
    private Long quizId;
    private String userChoice;

    public AnswerRequestDto() {}

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
}
