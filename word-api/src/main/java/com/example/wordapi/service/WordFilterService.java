package com.example.wordapi.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class WordFilterService {

    private final FrequencyService frequencyService;

    public WordFilterService(FrequencyService frequencyService) {
        this.frequencyService = frequencyService;
    }

    public List<String> filterTopWords(List<String> words, int threshold) {
        return words.stream()
            .distinct()
            .filter(word -> frequencyService.isHighPriority(word, threshold))
            .collect(Collectors.toList());
    }
}

