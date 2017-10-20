package org.crew102.rapidrake;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import opennlp.tools.stemmer.snowball.SnowballStemmer;
import org.crew102.rapidrake.model.*;

public class RakeAlgorithm {
	
	// Alg options
	// should these go in seperate class?
	private List<String> stopWords;
	private List<String> stopPOS;
	private int wordMinChar;
	private boolean stem;
	
	// Util objects (should be moved over to document and made static)
	private static Tagger tagger;
	private static SnowballStemmer stemmer;
	
	public RakeAlgorithm(String[] stopWords, String[] stopPOS, 
			int wordMinChar, boolean stem, Tagger tagger) throws java.io.IOException {
		this.stopWords = Arrays.asList(stopWords);
		this.stopPOS = Arrays.asList(stopPOS);
		this.wordMinChar = wordMinChar;
		this.stem = stem;
		this.tagger = tagger;
	}
	
	public RakeAlgorithm() {
		this.stopWords = Arrays.asList("i", "do", "be");
		this.stopPOS = Arrays.asList("VB", "VBD", "VBG", "VBN", "VBP", "VBZ");
		this.wordMinChar = 2;
		this.stem = true;
		this.tagger = new Tagger();
	}
	
	// Load utils
	static {
		stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
	}
	
	// Run rake
	public Result rake(Document aDoc) { 
//		
		// Create tokens
		aDoc.initTokens(stopPOS, wordMinChar, stopWords);
		
		String cleanedString = aDoc.collapseTokens();
		
		// Group tokens into keywords
		aDoc.initKeywords(cleanedString);
		
		// Stem tokens in keywords (move this into token creation?)
		
		if (stem) aDoc.stemKeywords(stemmer);
		
		// Calc token-level scores
		Map<String, Float> scoreVec = aDoc.calcTokenScores(stem);
		
		// Sum token-level scores for each keyword
		aDoc.sumKeywordScores(scoreVec, stem);
		
		return aDoc.returnResult();
	}
	
	
}