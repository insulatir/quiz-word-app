package com.example.wordapi.repository;

import com.example.wordapi.model.Word;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
	List<Word> findByLevel(String level);
	boolean existsByTermAndExample(String term, String example);
	Optional<Word> findByTermAndExample(String term, String example);
}
