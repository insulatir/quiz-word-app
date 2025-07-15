package com.example.wordapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WordFilterServiceTest {

    private FrequencyService frequencyService;
    private WordFilterService wordFilterService;

    @BeforeEach
    public void setup() {
        frequencyService = mock(FrequencyService.class);
        wordFilterService = new WordFilterService(frequencyService);
    }

    @Test
    public void testFilterTopWords_basicFiltering() {
        List<String> inputWords = List.of("猫", "犬", "学校", "猫", "勉強");

        // 빈도 높은 단어는 "猫", "学校"라고 가정
        when(frequencyService.isHighPriority("猫", 3000)).thenReturn(true);
        when(frequencyService.isHighPriority("犬", 3000)).thenReturn(false);
        when(frequencyService.isHighPriority("学校", 3000)).thenReturn(true);
        when(frequencyService.isHighPriority("勉強", 3000)).thenReturn(false);

        List<String> result = wordFilterService.filterTopWords(inputWords, 3000);

        assertEquals(List.of("猫", "学校"), result);
    }
}
