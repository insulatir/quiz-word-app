package com.example.wordapi.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class FrequencyService {
    private final Map<String, Integer> frequencyMap = new HashMap<>();

    @PostConstruct
    public void init() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("/frequency.tsv"), StandardCharsets.UTF_8))) {

            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split("\t");
                if (parts.length >= 3) {
                    String word = parts[2]; // lemma
                    String rankStr = parts[0];
                    try {
                        int rank = Integer.parseInt(rankStr);
                        frequencyMap.put(word, rank);
                    } catch (NumberFormatException e) {
                        // skip malformed line
                    }
                }
            }

        }
    }

    public Integer getFrequency(String word) {
        return frequencyMap.get(word);
    }

    public boolean isHighPriority(String word, int maxRank) {
        Integer rank = getFrequency(word);
        return rank != null && rank <= maxRank;
    }
}

