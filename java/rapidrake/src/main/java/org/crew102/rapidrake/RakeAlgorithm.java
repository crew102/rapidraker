package org.crew102.rapidrake;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import opennlp.tools.tokenize.*;
import opennlp.tools.postag.*;
import opennlp.tools.stemmer.snowball.SnowballStemmer;
import org.crew102.rapidrake.model.*;

public class RakeAlgorithm {
	
	// Alg options
	private List<String> stopWords;
	private List<String> stopPOS;
	private int wordMinChar;
	private boolean stem;
	
	// Util objects (should be moved over to document and made static)
	private Tokenizer tokenizer;
	private POSTaggerME tagger;
	private SnowballStemmer stemmer;
	
	public RakeAlgorithm(String[] stopWords, String[] stopPOS, 
			int wordMinChar, boolean stem) throws java.io.IOException {
		
		List<String> stopWordsArry = Arrays.asList(stopWords);
		this.stopWords = stopWordsArry;
		
		List<String> stopPosArry = Arrays.asList(stopPOS);
		this.stopPOS = stopPosArry;
		
		this.wordMinChar = wordMinChar;
		this.stem = stem;
	}
	
	// Load utils
	private void loadTokenizer() throws java.io.IOException {
		InputStream modelIn = new FileInputStream("model-bin/en-token.bin");
		TokenizerModel model = new TokenizerModel(modelIn);
		tokenizer = new TokenizerME(model);
	}
	private void loadPosTagger() throws java.io.IOException {
		InputStream modelIn2 = new FileInputStream("model-bin/en-pos-maxent.bin");
		POSModel model2 = new POSModel(modelIn2);
		tagger = new POSTaggerME(model2);
	}
	private void loadStemmer() throws java.io.IOException {
		stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
	}
	public void loadAllFuns() throws java.io.IOException {
		loadTokenizer();
		loadPosTagger();
		loadStemmer();
	}
	
	// Run rake
	public void rake(Document aDoc) {
		
		// Create tokens
		aDoc.initTokens(tokenizer, tagger, stopPOS, wordMinChar, stopWords);
		
		// Group tokens into keywords
		aDoc.genKeywords();
		
		// Stem tokens in keywords
		if (stem) {
			aDoc.stemKeywords(stemmer);
		}
		
		// Calc token-level scores
		Map<String, Float> scoreVec = aDoc.calcTokenScores(stem);
		
		// Sum token-level scores for each keyword
		aDoc.sumKeywordScores(scoreVec, stem);
		
	}
	
}