package com.example.wordapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
	    name = "word",
	    uniqueConstraints = {
	        @UniqueConstraint(columnNames = {"term", "example"})
	    }
	)
public class Word {
    @Id 
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String term;
    private String meaning;
    @Column(unique = true)
    private String example;
    private String level;

    public Word() {}

    public Word(String term, String meaning, String example, String level) {
        this.term = term;
        this.meaning = meaning;
        this.example = example;
        this.level = level;
    }

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

	public String getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = example;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
}
