package org.crew102.rapidrake.model;

import opennlp.tools.stemmer.snowball.SnowballStemmer;

// A Token is a unigram...It is referred to as a "word" in slowraker 

public class Token {
	
	private static final SnowballStemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
	
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
	
	public void stem() {
		this.stemmedForm = stemmer.stem(this.fullForm).toString();
	}
	
}