package org.crew102.rapidrake.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;

public class Document {
	
	private String txtEl;
	private ArrayList<Token> tokens = new ArrayList<Token>();
	private ArrayList<Keyword> keywords = new ArrayList<Keyword>();

	public Document(String txt) {
		this.txtEl = txt;
	}
	
	public void initTokens(RakeParams rakeParams, POSTaggerME tagger) {
		
		String[] tokenArray = SimpleTokenizer.INSTANCE.tokenize(txtEl);
		String[] tagArray = tagger.tag(tokenArray);

		for (int i = 0; i < tokenArray.length; i++) {
			
			String token = tokenArray[i];
			String tag = tagArray[i];
						
			if (rakeParams.getStopPOS().contains(tag) | token.length() < rakeParams.getWordMinChar() | rakeParams.getStopPOS().contains(token)) {
				Token x = new Token(".");
				tokens.add(x);
			} else {
				Token x = new Token(token.toLowerCase());
				if (rakeParams.shouldStem()) {
					x.stem();
				}
				tokens.add(x);
			}
		}
	}
	
	public void initKeywords() {
		
		String cleanedTxt = collapseTokens();
		String[] aryKey = cleanedTxt.split("[,.?():;\"-]");
		Pattern anyWordChar = Pattern.compile("[a-z]");
		
		for (int i = 0; i < aryKey.length; i++) {
			String oneKey = aryKey[i];
			Matcher myMatch = anyWordChar.matcher(oneKey);
			if (myMatch.find()) {
				String trimmedKey = oneKey.trim();
				String[] wordAr = trimmedKey.split(" ");
				Keyword someKey = new Keyword(trimmedKey, wordAr);
				keywords.add(someKey);
			}
		}
	}
	
	public String collapseTokens() {
		
		StringBuilder fullBuff = new StringBuilder();
		
		for (int i = 0; i < tokens.size(); i++) {
			Token atok = tokens.get(i);	
			String toAdd = atok.getFullForm() + " ";
			fullBuff.append(toAdd);
		}
	
		return fullBuff.toString();
	}
	
	public void calculateScores(RakeParams rakeParams) {
		
		// Calc token-level scores
		Map<String, Float> scoreVec = calcTokenScores(rakeParams);
		
		// Sum token-level scores for each keyword
		sumKeywordScores(scoreVec, rakeParams);
	}
	
	private Map<String, Float> calcTokenScores(RakeParams rakeParams) {
		 
		 Map<String, Integer> wordfreq = new HashMap<String, Integer>();
		 Map<String, Integer> worddegTemp = new HashMap<String, Integer>();
		 Map<String, Float> scores = new HashMap<String, Float>();
		 
		 for (int i = 0; i < keywords.size(); i++) {
			 Keyword oneKey = keywords.get(i);
			 String[] keysTokens;
			 if (rakeParams.shouldStem()) {
				 keysTokens = oneKey.getKeyStemmedAry();
			 } else {
				 keysTokens = oneKey.getKeyStringAry();
			 }
			 
			 for (int z = 0; z < keysTokens.length; z++) {
				 
				 String aTok = keysTokens[z];
				 int degTe = keysTokens.length - 1;
				 
				 if (!wordfreq.containsKey(aTok)) {
					 wordfreq.put(aTok, 1);
					 worddegTemp.put(aTok, degTe);
				 } else {
					 int valu2 = wordfreq.get(aTok) + 1;
					 wordfreq.replace(aTok, valu2);
					 int repdeg = worddegTemp.get(aTok) + degTe;
					 worddegTemp.replace(aTok, repdeg);
				 }
			 }
		 }

		 for (Map.Entry<String, Integer> entry : wordfreq.entrySet()) {
			 String aKey = entry.getKey();
			 float freq = (float) wordfreq.get(aKey);
			 float val = (worddegTemp.get(aKey) + freq) / freq;
			 scores.put(aKey, val);
		}
		 
		 return scores;
	}
	
	private void sumKeywordScores(Map<String, Float> scoreVec, RakeParams rakeParams) {
		
		 for (int i = 0; i < keywords.size(); i++) {
			 Keyword oneKey = keywords.get(i);
			 oneKey.sumScore(scoreVec, rakeParams);
		 }
	}
	
	public Result returnResult() {
		
		String[] full = new String[keywords.size()];
		String[] stemmed = new String[keywords.size()];
		float[] scores = new float[keywords.size()];
		
		for (int i = 0; i < keywords.size(); i++) {
			Keyword oneKey = keywords.get(i);
			full[i] = oneKey.getKeyString();
			stemmed[i] = oneKey.getStemmedString();
			scores[i] = oneKey.getScore();			
		}
		
		return new Result(full, stemmed, scores);
	}
	 
}