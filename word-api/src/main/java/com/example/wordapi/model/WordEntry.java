package com.example.wordapi.model;

public class WordEntry {
    private String term;
    private String meaning;
    private String example;
    private int level;

    public WordEntry(String term, String meaning, String example, int level) {
        this.term = term;
        this.meaning = meaning;
        this.example = example;
        this.level = level;
    }
    
    public String[] toCsvRow() {
        return new String[] { term, meaning, example, String.valueOf(level) };
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

    
}
