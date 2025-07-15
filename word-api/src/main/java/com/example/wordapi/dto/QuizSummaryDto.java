package com.example.wordapi.dto;

public class QuizSummaryDto {
    private int total;
    private int correct;
    private int incorrect;

    public QuizSummaryDto(int total, int correct, int incorrect) {
        this.total = total;
        this.correct = correct;
        this.incorrect = incorrect;
    }

    public int getTotal() {
        return total;
    }

    public int getCorrect() {
        return correct;
    }

    public int getIncorrect() {
        return incorrect;
    }
}
