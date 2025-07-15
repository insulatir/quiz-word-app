package com.example.wordapi.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import com.example.wordapi.model.WordEntry;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class WordJsonBuilderService {

    private final MorphologicalAnalyzer morphologicalAnalyzer;
    private final FrequencyService frequencyService;

    public WordJsonBuilderService(MorphologicalAnalyzer analyzer, FrequencyService frequencyService) {
        this.morphologicalAnalyzer = analyzer;
        this.frequencyService = frequencyService;
    }

    public List<WordEntry> buildJson(String articleText) {
        List<String> words = morphologicalAnalyzer.extractWords(articleText);
        Set<String> uniqueWords = new HashSet<>(words);

        List<WordEntry> result = new ArrayList<>();
        for (String word : uniqueWords) {
            Integer rank = frequencyService.getFrequency(word);
            if (rank != null && rank <= 6000) {  // 예: 상위 6000위까지만 필터링
                int level = rank == null ? 9999 :(rank - 1) / 3000 + 1;  // 0~2999 → 1, 3000~5999 → 2
                String example = findExampleSentence(articleText, word);
                result.add(new WordEntry(word, "", example, level));
            }
            System.out.println("추출된 단어: " + word + " 빈도: " + rank);

        }

        return result;
    }

    public void writeToCsv(List<WordEntry> entries, String filePath) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath), StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("term", "meaning", "example", "level"))) {

            for (WordEntry entry : entries) {
                csvPrinter.printRecord(entry.toCsvRow());
            }
        }
    }

    private String findExampleSentence(String text, String word) {
        for (String sentence : text.split("[。！？\n]")) {
            if (sentence.contains(word)) {
                return sentence.trim();
            }
        }
        return "";
    }
}

