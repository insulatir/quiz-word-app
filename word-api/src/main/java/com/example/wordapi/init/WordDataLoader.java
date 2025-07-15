package com.example.wordapi.init;

import com.example.wordapi.model.Word;
import com.example.wordapi.repository.WordRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
public class WordDataLoader implements CommandLineRunner {
    private final WordRepository wordRepository;

    public WordDataLoader(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("words.csv");

        if (inputStream == null) {
            System.out.println("CSV 파일이 classpath에서 발견되지 않았습니다.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            reader.readLine(); // 헤더 스킵

            reader.lines()
                    .map(line -> line.split(",", -1))
                    .filter(parts -> parts.length == 4)
                    .forEach(parts -> {
                        String term = parts[0].trim();
                        String meaning = parts[1].trim();
                        String example = parts[2].trim();
                        String level = parts[3].trim();

                        wordRepository.findByTermAndExample(term, example)
                                .ifPresentOrElse(
                                        existingWord -> {
                                            // 이미 있으면 업데이트
                                            existingWord.setMeaning(meaning);
                                            existingWord.setLevel(level);
                                            wordRepository.save(existingWord);
                                        },
                                        () -> {
                                            // 없으면 새로 저장
                                            Word newWord = new Word(term, meaning, example, level);
                                            wordRepository.save(newWord);
                                        });
                    });
        }
    }
}
