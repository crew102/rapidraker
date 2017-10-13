package org.crew102.rapidrake.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.stemmer.snowball.SnowballStemmer;
import opennlp.tools.tokenize.*;
import org.crew102.rapidrake.model.Token;

public class Document {
	
	private String txtEl;
	private ArrayList<Token> tokens;
	private ArrayList<Keyword> keywords;

	public Document(String txt) {
		this.txtEl = txt;
		this.tokens = new ArrayList<Token>();
		this.keywords = new ArrayList<Keyword>();
	}
	
	public void initTokens(Tokenizer tokenizer, POSTaggerME tagger,
			List<String> stopPOSArry, int wordMinChar, List<String> stopWords) {
		
		String[] tokenArray = tokenizer.tokenize(txtEl);
		String[] tagArray = tagger.tag(tokenArray);

		for (int i = 0; i < tokenArray.length; i++) {
			String token = tokenArray[i];
			String tag = tagArray[i];
			
			if (stopPOSArry.contains(tag) | token.length() < wordMinChar | stopWords.contains(token)) {
				Token x = new Token(".");
				tokens.add(x);
			} else {
				Token x = new Token(token.toLowerCase());
				tokens.add(x);
			}
		}
	}
	
	public void genKeywords() {
		
		StringBuilder fullBuff = new StringBuilder();
		
		for (int i = 0; i < tokens.size(); i++) {
			Token atok = tokens.get(i);	
			String toAdd = atok.getFullForm() + " ";
			fullBuff.append(toAdd);
		}
	
		String fullString = fullBuff.toString();
		String[] aryKey = fullString.split("[,.?():;\"-]");
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
	
	public void stemKeywords(SnowballStemmer stemmer) {
		
		 for (int i = 0; i < keywords.size(); i++) {
			 Keyword oneKey = keywords.get(i);
			 oneKey.stem(stemmer);
		 }
	}
	
	public void sumKeywordScores(Map<String, Float> scoreVec, boolean stem) {
		
		 for (int i = 0; i < keywords.size(); i++) {
			 Keyword oneKey = keywords.get(i);
			 oneKey.sumScore(scoreVec, stem);
		 }
	}
	
	 public Map<String, Float> calcTokenScores(boolean stem) {
		 
		 Map<String, Integer> wordfreq = new HashMap<String, Integer>();
		 Map<String, Integer> worddegTemp = new HashMap<String, Integer>();
		 Map<String, Float> scores = new HashMap<String, Float>();
		 
		 for (int i = 0; i < keywords.size(); i++) {
			 Keyword oneKey = keywords.get(i);
			 String[] keysTokens;
			 if (stem) {
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
	 
//	 public String[][] getResults() {
//		 int keySize = keywords.size();
//		 String[][] keyRes = new String[keySize][3];
//		 for (int i = 0; i < keyRes.length; i++ ) {
//			 Keyword x = keywords.get(i);
//			 keyRes[i][0] = x.getKeyString();
//			 keyRes[i][1] = x.getStemmedString(); // need to actually create non-empty version
//			 keyRes[i][2] = x.getScore();
//		 }
//		 return keyRes;
//	 }
	 
}