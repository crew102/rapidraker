package org.crew102.rapidrake;

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
		
		// Group tokens into keywords
		aDoc.initKeywords();
		
		// Calculate keyword scores
		aDoc.calculateScores(rakeParams);
		
		// Return result object
		return aDoc.returnResult();
	}
	
}