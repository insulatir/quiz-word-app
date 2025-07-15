package com.example.wordapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wordapi.model.Word;
import com.example.wordapi.service.WordService;

@RestController
@RequestMapping("/words")
public class WordController {

	@Autowired
    private final WordService wordService;

    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @PostMapping
    public Word addWord(@RequestBody Word word) {
    	System.out.println("🔥 term: " + word.getTerm());
        System.out.println("🔥 meaning: " + word.getMeaning());
        System.out.println("🔥 example: " + word.getExample());
        return wordService.addWord(word);
    }


    @GetMapping
    public List<Word> getWords() {
        return wordService.getWords();
    }
}
