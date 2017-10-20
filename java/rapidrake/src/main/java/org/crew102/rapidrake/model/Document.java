package org.crew102.rapidrake.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import opennlp.tools.stemmer.snowball.SnowballStemmer;
import opennlp.tools.tokenize.SimpleTokenizer;
import org.crew102.rapidrake.model.Token;

public class Document {
	
	private String txtEl;
	private ArrayList<Token> tokens = new ArrayList<Token>();
	private ArrayList<Keyword> keywords = new ArrayList<Keyword>();
	
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

	public Document(String txt) {
		this.txtEl = txt;
	}
	
	public void initTokens(List<String> stopPOSArry, int wordMinChar, List<String> stopWords) {
		
		SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE; 
		String[] tokenArray = tokenizer.tokenize(txtEl);
		String[] tagArray = Tagger.posTagger.tag(tokenArray);

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
	
	public String collapseTokens() {
		
		StringBuilder fullBuff = new StringBuilder();
		
		for (int i = 0; i < tokens.size(); i++) {
			Token atok = tokens.get(i);	
			String toAdd = atok.getFullForm() + " ";
			fullBuff.append(toAdd);
		}
	
		return fullBuff.toString();
	}
	
	public void initKeywords(String cleanedTxt) {
		
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
	 
}