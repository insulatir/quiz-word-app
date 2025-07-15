package com.example.wordapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.wordapi.model.Word;
import com.example.wordapi.repository.WordRepository;

@Service
public class WordService {

	@Autowired
    private final WordRepository repo;

    public WordService(WordRepository repo) {
        this.repo = repo;
    }

    public Word addWord(Word word) {
        return repo.save(word);
    }

    public List<Word> getWords() {
        return repo.findAll();
    }
}
