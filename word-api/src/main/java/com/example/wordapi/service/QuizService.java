package com.example.wordapi.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.wordapi.dto.AnswerRequestDto;
import com.example.wordapi.dto.AnswerResponseDto;
import com.example.wordapi.dto.QuizDto;
import com.example.wordapi.dto.QuizResponseDto;
import com.example.wordapi.dto.QuizSummaryDto;
import com.example.wordapi.dto.SessionQuizItem;
import com.example.wordapi.dto.WrongNote;
import com.example.wordapi.model.Quiz;
import com.example.wordapi.model.QuizResult;
import com.example.wordapi.model.Word;
import com.example.wordapi.repository.QuizRepository;
import com.example.wordapi.repository.QuizResultRepository;
import com.example.wordapi.repository.WordRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class QuizService {
    private final WordRepository wordRepository;
    private final QuizRepository quizRepository;
    private QuizResultRepository quizResultRepository;
    private final Random random = new Random();

    public QuizService(WordRepository wordRepository, QuizRepository quizRepository, QuizResultRepository quizResultRepository) {
        this.wordRepository = wordRepository;
        this.quizRepository = quizRepository;
        this.quizResultRepository = quizResultRepository;
    }

    public QuizDto generateQuiz(String level) {
        List<Word> words = wordRepository.findByLevel(level);
        Collections.shuffle(words);

        Word correct = words.get(0);
        Set<String> choices = new HashSet<>();
        choices.add(correct.getTerm());

        int i = 1;
        while (choices.size() < 4 && i < words.size()) {
            choices.add(words.get(i++).getTerm());
        }

        List<String> choiceList = new ArrayList<>(choices);
        Collections.shuffle(choiceList);

        String sentence = correct.getExample().replace(correct.getTerm(), "(___)");
        Quiz quiz = new Quiz(sentence, choiceList, correct.getTerm());
        quizRepository.save(quiz);

        return new QuizDto(quiz.getId(), sentence, choiceList);
    }

    public List<QuizResponseDto> getAllQuizzes() {
        return quizRepository.findAll().stream()
            .map(quiz -> new QuizResponseDto(
                quiz.getId(),
                quiz.getSentence(),
                quiz.getChoices()
            ))
            .collect(Collectors.toList());
    }

    public long getQuizCount() {
        return quizRepository.count();
    }

    public List<QuizResult> getResults() {
        return quizResultRepository.findAllByOrderByTimestampDesc();
    }

    public QuizSummaryDto getQuizSummary() {
        List<QuizResult> allResults = quizResultRepository.findAll();
        int total = allResults.size();
        int correct = (int) allResults.stream().filter(QuizResult::isCorrect).count();
        int incorrect = total - correct;
        return new QuizSummaryDto(total, correct, incorrect);
    }

    public List<QuizResponseDto> getWrongQuizzes() {
        List<QuizResult> wrongResults = quizResultRepository.findAll()
                .stream()
                .filter(result -> !result.isCorrect())
                .collect(Collectors.toList());

        List<Long> wrongQuizIds = wrongResults.stream()
                .map(QuizResult::getQuizId)
                .distinct()
                .collect(Collectors.toList());

        List<Quiz> wrongQuizzes = quizRepository.findAllById(wrongQuizIds);

        return wrongQuizzes.stream()
                .map(q -> new QuizResponseDto(q.getId(), q.getSentence(), q.getChoices()))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public ResponseEntity<?> getNextQuiz(HttpSession session) {
        List<SessionQuizItem> sessionQuizzes = (List<SessionQuizItem>) session.getAttribute("sessionQuizzes");
        Set<Long> solved = (Set<Long>) session.getAttribute("solvedIds");

        if (sessionQuizzes == null || solved == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "세션 없음"));
        }

        for (SessionQuizItem item : sessionQuizzes) {
            if (!solved.contains(item.getQuizId())) {
                Quiz quiz = quizRepository.findById(item.getQuizId()).orElseThrow();

                return ResponseEntity.ok(Map.of(
                	    "quizId", quiz.getId(),
                	    "sentence", quiz.getSentence(),
                	    "choices", item.getShuffledChoices(),
                	    "finished", false,
                	    "totalCount", sessionQuizzes.size(),
                	    "solvedCount", solved.size()
                ));
            }
        }

        return ResponseEntity.ok(Map.of("finished", true, "totalCount", sessionQuizzes.size()));
    }

    public boolean isFinished(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Long> quizIds = (List<Long>) session.getAttribute("quizIds");
        @SuppressWarnings("unchecked")
        Set<Long> solved = (Set<Long>) session.getAttribute("solvedIds");

        if (quizIds == null || solved == null) return false;

        return solved.size() >= quizIds.size();
    }

    public AnswerResponseDto checkAnswer(AnswerRequestDto dto, HttpSession session) {
        Quiz quiz = quizRepository.findById(dto.getQuizId())
                .orElseThrow(() -> new RuntimeException("퀴즈 없음"));

        boolean isCorrect = quiz.getAnswer().equals(dto.getUserChoice());

        @SuppressWarnings("unchecked")
        Set<Long> solved = (Set<Long>) session.getAttribute("solvedIds");
        @SuppressWarnings("unchecked")
        List<SessionQuizItem> sessionQuizzes = (List<SessionQuizItem>) session.getAttribute("sessionQuizzes");

        if (solved != null && !solved.contains(dto.getQuizId())) {
            solved.add(dto.getQuizId());
            session.setAttribute("solvedIds", solved);
        }

        quizResultRepository.save(new QuizResult(dto.getQuizId(), dto.getUserChoice(), isCorrect));

        boolean isFinished = solved != null && sessionQuizzes != null && solved.size() >= sessionQuizzes.size();

        String msg = isCorrect ? "정답입니다!" : "오답입니다. 정답은 " + quiz.getAnswer() + "입니다。";

        return new AnswerResponseDto(isCorrect, msg, isFinished);
    }

    public ResponseEntity<?> startSession(HttpSession session) {
        List<Quiz> all = quizRepository.findAll();
        Collections.shuffle(all);

        List<Quiz> selected = all.stream().limit(10).collect(Collectors.toList());

        List<SessionQuizItem> sessionQuizItems = selected.stream()
            .map(q -> new SessionQuizItem(
                q.getId(),
                shuffleChoices(q.getChoices())
            ))
            .toList();

        session.setAttribute("sessionQuizzes", sessionQuizItems);
        session.setAttribute("solvedIds", new HashSet<Long>());

        return ResponseEntity.ok().build();
    }

    private List<String> shuffleChoices(List<String> choices) {
        List<String> copy = new ArrayList<>(choices);
        Collections.shuffle(copy);
        return copy;
    }

    public ResponseEntity<?> getSessionQuizList(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<SessionQuizItem> sessionQuizzes = (List<SessionQuizItem>) session.getAttribute("sessionQuizzes");
        if (sessionQuizzes == null) {
            return ResponseEntity.badRequest().body("세션 없음");
        }

        List<Map<String, Object>> list = sessionQuizzes.stream()
            .map(item -> {
                Quiz quiz = quizRepository.findById(item.getQuizId()).orElseThrow();
                return Map.of(
                    "quizId", quiz.getId(),
                    "sentence", quiz.getSentence(),
                    "choices", item.getShuffledChoices()
                );
            })
            .toList();

        return ResponseEntity.ok(list);
    }
    
    public ResponseEntity<?> getSessionState(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<SessionQuizItem> sessionQuizzes = (List<SessionQuizItem>) session.getAttribute("sessionQuizzes");
        @SuppressWarnings("unchecked")
        Set<Long> solved = (Set<Long>) session.getAttribute("solvedIds");

        if (sessionQuizzes == null || solved == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "세션 없음"));
        }

        // 현재 문제
        for (int i = 0; i < sessionQuizzes.size(); i++) {
            SessionQuizItem item = sessionQuizzes.get(i);
            if (!solved.contains(item.getQuizId())) {
                Quiz quiz = quizRepository.findById(item.getQuizId()).orElseThrow();
                return ResponseEntity.ok(Map.of(
                    "quizId", quiz.getId(),
                    "sentence", quiz.getSentence(),
                    "choices", item.getShuffledChoices(),
                    "currentIndex", i,
                    "totalCount", sessionQuizzes.size(),
                    "finished", false
                ));
            }
        }

        return ResponseEntity.ok(Map.of("finished", true, "totalCount", sessionQuizzes.size()));
    }
    
    public List<Quiz> getFavoritesWithId(List<Long> favIds) {
    	return quizRepository.findAllById(favIds);
    }

    @SuppressWarnings("unchecked")
    public void toggleFavorite(Long quizId, HttpSession session) {
        Set<Long> favorites = (Set<Long>) session.getAttribute("favoriteQuizIds");

        if (favorites == null) {
            favorites = new HashSet<>();
        }

        if (favorites.contains(quizId)) {
            favorites.remove(quizId);
        } else {
            favorites.add(quizId);
        }

        session.setAttribute("favoriteQuizIds", favorites);
    }

    @SuppressWarnings("unchecked")
    public List<Long> getFavorites(HttpSession session) {
        Set<Long> favorites = (Set<Long>) session.getAttribute("favoriteQuizIds");
        if (favorites == null) {
            return List.of();
        }
        return new ArrayList<>(favorites);
    }
    
    public String initSampleQuizzes() {
        quizRepository.deleteAll();

        List<Quiz> sampleQuizzes = List.of(
            new Quiz("新しい(___)を構築する必要がある。", List.of("制度", "法律", "方法", "理論"), "制度"),
            new Quiz("その考え方には(___)がある。", List.of("矛盾", "真実", "同意", "利点"), "矛盾"),
            new Quiz("政府は新しい法律を(___)。", List.of("制定した", "翻訳した", "延長した", "削除した"), "制定した"),
            new Quiz("彼の意見は(___)に基づいている。", List.of("事実", "感情", "夢", "予想"), "事実"),
            new Quiz("その計画には多くの(___)がある。", List.of("問題", "解決", "魅力", "答え"), "問題"),
            new Quiz("この制度は今後も(___)される予定だ。", List.of("維持", "終了", "破壊", "削除"), "維持"),
            new Quiz("新しい技術が社会に(___)を与えた。", List.of("影響", "報酬", "苦痛", "声援"), "影響"),
            new Quiz("その(___)は十分ではない。", List.of("説明", "投資", "距離", "価格"), "説明"),
            new Quiz("環境問題に(___)する。", List.of("対応", "依存", "放置", "反発"), "対応"),
            new Quiz("結果は(___)通りだった。", List.of("予想", "努力", "希望", "経験"), "予想")
        );

        quizRepository.saveAll(sampleQuizzes);
        return "샘플 퀴즈 10개를 등록했습니다.";
    }
}
