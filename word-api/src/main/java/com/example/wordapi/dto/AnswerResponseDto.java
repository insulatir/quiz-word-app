package com.example.wordapi.dto;

public class AnswerResponseDto {
    private boolean correct;
    private String message;
    private boolean finished;

    public AnswerResponseDto(boolean correct, String message, boolean finished) {
        this.correct = correct;
        this.message = message;
        this.finished = finished;
    }

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean getFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
}

