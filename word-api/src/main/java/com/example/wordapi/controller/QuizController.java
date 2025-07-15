package com.example.wordapi.controller;

import com.example.wordapi.dto.AnswerRequestDto;
import com.example.wordapi.dto.AnswerResponseDto;
import com.example.wordapi.dto.QuizDto;
import com.example.wordapi.dto.QuizResponseDto;
import com.example.wordapi.dto.QuizSummaryDto;
import com.example.wordapi.model.Quiz;
import com.example.wordapi.model.QuizResult;
import com.example.wordapi.service.QuizService;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/quiz")
public class QuizController {
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    public QuizDto getQuiz(@RequestParam(defaultValue = "1") String level) {
        return quizService.generateQuiz(level);
    }

    @PostMapping("/answer")
    public AnswerResponseDto checkAnswer(@RequestBody AnswerRequestDto dto, HttpSession session) {
    	return quizService.checkAnswer(dto, session);
    }

    @GetMapping("/count")
    public int getQuizCount() {
        return (int) quizService.getQuizCount();
    }

    @GetMapping("/results")
    public List<QuizResult> getResults() {
    	return quizService.getResults();
    }

    @GetMapping("/summary")
    public QuizSummaryDto getQuizSummary() {
        return quizService.getQuizSummary();
    }
    
    @GetMapping("/wrong")
    public List<QuizResponseDto> getWrongQuizzes() {
        return quizService.getWrongQuizzes();
    }

    // 퀴즈 제공
    @GetMapping("/")
    public ResponseEntity<?> getQuiz(HttpSession session) {
        return quizService.getNextQuiz(session);
    }
    
    @PostMapping("/session/start")
    public ResponseEntity<?> startSession(HttpSession session) {
        return quizService.startSession(session);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getSessionQuizList(HttpSession session) {
        return quizService.getSessionQuizList(session);
    }

    @GetMapping("/session/state")
    public ResponseEntity<?> getSessionState(HttpSession session) {
        return quizService.getSessionState(session);
    }
    
    @PostMapping("/favorite/toggle")
    public ResponseEntity<?> toggleFavorite(@RequestBody Map<String, Long> body, HttpSession session) {
        Long quizId = body.get("quizId");
        quizService.toggleFavorite(quizId, session);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<?> getFavorites(HttpSession session) {
        List<Long> favIds = quizService.getFavorites(session);
        List<Quiz> quizzes = quizService.getFavoritesWithId(favIds);
        return ResponseEntity.ok(quizzes);
    }
    
 // QuizController에 추가 (임시)
    @PostMapping("/init")
    public void deleteAll() {
        quizService.initSampleQuizzes();
    }

}

