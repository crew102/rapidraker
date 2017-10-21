package org.crew102.rapidrake;

import org.crew102.rapidrake.model.*;

import opennlp.tools.postag.POSTaggerME;

public class RakeAlgorithm {
	
	private final RakeParams rakeParams;
	private final POSTaggerME tagger;
	
	public RakeAlgorithm(RakeParams rakeParams, String taggerModelUrl)  throws java.io.IOException {
		this.rakeParams = rakeParams;
		this.tagger = new Tagger(taggerModelUrl).getPosTagger();
	}
	
	public RakeAlgorithm() throws java.io.IOException {
		String[] stopWords = {"i", "do", "be"};
		String[] stopPOS = {"VB", "VBD", "VBG", "VBN", "VBP", "VBZ"};
		this.rakeParams = new RakeParams(stopWords, stopPOS, 2, true);
		this.tagger = new Tagger().getPosTagger();
	}
	
	// Run rake
	public Result rake(Document aDoc) { 
//		
		// Create tokens
		aDoc.initTokens(rakeParams, tagger);
		
		// Group tokens into keywords
		aDoc.initKeywords();
		
		// Calculate keyword scores
		aDoc.calculateScores(rakeParams);
		
		// Return result object
		return aDoc.returnResult();
	}
	
}