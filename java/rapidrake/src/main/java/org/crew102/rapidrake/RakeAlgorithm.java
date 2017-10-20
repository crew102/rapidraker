package org.crew102.rapidrake;

import java.util.Map;
import org.crew102.rapidrake.model.*;

public class RakeAlgorithm {
	
	private final RakeParams rakeParams;
	private static Tagger tagger;
	
	public RakeAlgorithm(RakeParams rakeParams, Tagger tagger) {
		this.rakeParams = rakeParams;
		this.tagger = tagger;
	}
	
	public RakeAlgorithm() {
		String[] stopWords = {"i", "do", "be"};
		String[] stopPOS = {"VB", "VBD", "VBG", "VBN", "VBP", "VBZ"};
		this.rakeParams = new RakeParams(stopWords, stopPOS, 2, true);
		this.tagger = new Tagger();
	}
	
	// Run rake
	public Result rake(Document aDoc) { 
//		
		// Create tokens
		aDoc.initTokens(rakeParams);
		
		String cleanedString = aDoc.collapseTokens();
		
		// Group tokens into keywords
		aDoc.initKeywords(cleanedString);
		
		// Calc token-level scores
		Map<String, Float> scoreVec = aDoc.calcTokenScores(rakeParams);
		
		// Sum token-level scores for each keyword
		aDoc.sumKeywordScores(scoreVec, rakeParams);
		
		return aDoc.returnResult();
	}
	
	
}