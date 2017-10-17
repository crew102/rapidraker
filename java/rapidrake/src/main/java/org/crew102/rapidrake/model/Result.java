package org.crew102.rapidrake.model;

public class Result {
	
	private String[] fullKeywords;
	private String[] stemmedKeywords;
	private float[] scores;
	
	public Result(String[] fullKeywords, String[] stemmedKeywords, float[] scores) {
		this.fullKeywords = fullKeywords;
		this.stemmedKeywords = stemmedKeywords;
		this.scores = scores;
	}

	public String[] getFullKeywords() {
		return fullKeywords;
	}
	public String[] getStemmedKeywords() {
		return stemmedKeywords;
	}
	public float[] getScores() {
		return scores;
	}
	
}
