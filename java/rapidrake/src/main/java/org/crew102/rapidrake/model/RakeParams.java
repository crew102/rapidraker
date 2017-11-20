package org.crew102.rapidrake.model;

import java.util.Arrays;
import java.util.List;

// A RakeParams object holds the various parameters the user can set when running RAKE

public class RakeParams {
	
	private final List<String> stopWords;
	private final List<String> stopPOS;
	private final int wordMinChar;
	private final boolean stem;
	
	public RakeParams(String[] stopWords, String[] stopPOS, int wordMinChar, boolean stem) {
		this.stopWords = Arrays.asList(stopWords);
		this.stopPOS = Arrays.asList(stopPOS);
		this.wordMinChar = wordMinChar;
		this.stem = stem;
	}
	
	public List<String> getStopWords() {
		return stopWords;
	}
	public List<String> getStopPOS() {
		return stopPOS;
	}
	public int getWordMinChar() {
		return wordMinChar;
	}
	public boolean shouldStem() {
		return stem;
	}

}