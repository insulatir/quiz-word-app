package com.example.wordapi.model;

import java.util.ArrayList;
import java.util.List;

import com.example.wordapi.dto.QuizDto;
import com.example.wordapi.dto.WrongNote;

import lombok.Data;

@Data
public class QuizSession {
    private List<QuizDto> quizList = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;
    private List<WrongNote> wrongList = new ArrayList<>();
}
