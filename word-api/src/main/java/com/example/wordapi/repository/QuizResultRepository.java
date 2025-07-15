package com.example.wordapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wordapi.model.QuizResult;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
    List<QuizResult> findAllByOrderByTimestampDesc();
}

