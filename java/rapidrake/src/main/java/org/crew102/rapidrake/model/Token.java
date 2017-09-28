package org.crew102.rapidrake.model;

// A Token is a unigram...It is referred to as a "word" in slowraker 

public class Token {
	
	private String fullForm;
	private String stemmedForm;
	
	private float score;
	private int freq;
	private int degree;
	
	public Token(String fullForm) {
		this.fullForm = fullForm;
	}
	
	public String getFullForm() {
		return fullForm;
	}
	
}