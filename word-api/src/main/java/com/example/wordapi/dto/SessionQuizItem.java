package com.example.wordapi.dto;

import java.util.List;

//DTO 역할
public class SessionQuizItem {
 private Long quizId;
 private List<String> shuffledChoices;

 public SessionQuizItem(Long quizId, List<String> shuffledChoices) {
     this.quizId = quizId;
     this.shuffledChoices = shuffledChoices;
 }

 public Long getQuizId() { return quizId; }
 public List<String> getShuffledChoices() { return shuffledChoices; }
}
