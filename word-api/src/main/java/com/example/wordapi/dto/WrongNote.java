package com.example.wordapi.dto;

public class WrongNote {
    private String sentence;
    private String correctAnswer;
    private String userChoice;

    public WrongNote(String sentence, String correctAnswer, String userChoice) {
        this.sentence = sentence;
        this.correctAnswer = correctAnswer;
        this.userChoice = userChoice;
    }

    // Getter 추가
    public String getSentence() {
        return sentence;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getUserChoice() {
        return userChoice;
    }
}
