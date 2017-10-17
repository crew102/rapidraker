package org.crew102.rapidrake;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.postag.POSModel;
import opennlp.tools.stemmer.snowball.SnowballStemmer;
import org.crew102.rapidrake.model.*;

public class RakeAlgorithm {
	
	// Alg options
	private List<String> stopWords;
	private List<String> stopPOS;
	private int wordMinChar;
	private boolean stem;
	
	// Util objects (should be moved over to document and made static)
	private static POSTaggerME tagger;
	private static SnowballStemmer stemmer;
	
	public RakeAlgorithm(String[] stopWords, String[] stopPOS, 
			int wordMinChar, boolean stem) throws java.io.IOException {
		
		List<String> stopWordsArry = Arrays.asList(stopWords);
		this.stopWords = stopWordsArry;
		
		List<String> stopPosArry = Arrays.asList(stopPOS);
		this.stopPOS = stopPosArry;
		
		this.wordMinChar = wordMinChar;
		this.stem = stem;
	}
	
	public RakeAlgorithm() {
		
		String[] stopWords = new String[] {"i", "do", "be"};
		List<String> stopWordsArry = Arrays.asList(stopWords);
		this.stopWords = stopWordsArry;
		
		String[] stopPOS = new String[] {"VB", "VBD", "VBG", "VBN", "VBP", "VBZ"};
		List<String> stopPosArry = Arrays.asList(stopPOS);
		this.stopPOS = stopPosArry;
		
		this.wordMinChar = 2;
		this.stem = true;
	}
	
	// Load utils
	static {
		stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
		
		InputStream modelIn2;
		POSModel model2 = null;
		
		try {
			modelIn2 = new FileInputStream("model-bin/en-pos-maxent.bin");
			model2 = new POSModel(modelIn2);
		} catch (IOException ex) {
			// need to handle exception
		}
		tagger = new POSTaggerME(model2);
	}
	
	// Run rake
	public Result rake(Document aDoc) { 
//		
		// Create tokens
		aDoc.initTokens(tagger, stopPOS, wordMinChar, stopWords);
		
		String cleanedString = aDoc.collapseTokens();
		
		// Group tokens into keywords
		aDoc.initKeywords(cleanedString);
		
		// Stem tokens in keywords
		
		if (stem) aDoc.stemKeywords(stemmer);
		
		// Calc token-level scores
		Map<String, Float> scoreVec = aDoc.calcTokenScores(stem);
		
		// Sum token-level scores for each keyword
		aDoc.sumKeywordScores(scoreVec, stem);
		
		return aDoc.returnResult();
		
	}
	
	
}