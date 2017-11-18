package org.crew102.rapidrake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.crew102.rapidrake.model.*;

import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.stemmer.snowball.SnowballStemmer;
import opennlp.tools.tokenize.SimpleTokenizer;

public class RakeAlgorithm {
	
	private static RakeParams rakeParams;
	
	private static POSTaggerME tagger;
	private static final SnowballStemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
		
	////
	
	public RakeAlgorithm(RakeParams rakeParams, String taggerModelUrl)  throws java.io.IOException {
		RakeAlgorithm.rakeParams = rakeParams;
		RakeAlgorithm.tagger = new Tagger(taggerModelUrl).getPosTagger();
	}
	
	public RakeAlgorithm() throws java.io.IOException {
		String[] stopWords = {"i", "do", "be"};
		String[] stopPOS = {"VB", "VBD", "VBG", "VBN", "VBP", "VBZ"};
		RakeAlgorithm.rakeParams = new RakeParams(stopWords, stopPOS, 2, true);
		RakeAlgorithm.tagger = new Tagger().getPosTagger();
	}
	
	////
	
	public Result rake(String txtEl) {
		String[] tokens = tokenize(txtEl);
		String[] tags = tag(tokens, tagger);
		String[] modTokens = replaceUnwantedTokens(tokens, tags, rakeParams);
		ArrayList<Keyword> keywords = extractKeywords(modTokens, rakeParams);
		return getResult(keywords);
	}
	
	private String[] tokenize(String txtEl) {
		return SimpleTokenizer.INSTANCE.tokenize(txtEl.toLowerCase());
	}
	
	private String[] tag(String[] tokens, POSTaggerME tagger) {
		return tagger.tag(tokens);
	}
	
	private String[] replaceUnwantedTokens(String[] tokens, String[] tags, RakeParams rakeParams) {
		
		for (int i = 0; i < tokens.length; i++) {
			
			String oneToken = tokens[i];
			String oneTag = tags[i];
			
			Pattern anyWordChar = Pattern.compile("[a-z]");
			boolean noAlphaChars = !anyWordChar.matcher(oneToken).find();
			
			if (rakeParams.getStopPOS().contains(oneTag) || oneToken.length() < rakeParams.getWordMinChar() || rakeParams.getStopWords().contains(oneToken) || noAlphaChars) {
				tokens[i] = ".";
			}
		}
		return tokens;
	}
	
	public ArrayList<Keyword> extractKeywords(String[] modTokens, RakeParams rakeParams) {
		ArrayList<Keyword> keywords = idCandidateKeywords(modTokens, rakeParams);
		return calcKeywordScores(keywords, rakeParams);
	}
	
	private ArrayList<Keyword> idCandidateKeywords(String[] tokens, RakeParams rakeParams) {
		
		ArrayList<Keyword> keywords = new ArrayList<Keyword>();
		String cleanedTxt = collapseTokens(tokens);
		String[] aryKey = cleanedTxt.split("[,.?():;\"-'/]");
		Pattern anyWordChar = Pattern.compile("[a-z]");
		
		for (int i = 0; i < aryKey.length; i++) {
			String oneKey = aryKey[i];
			Matcher myMatch = anyWordChar.matcher(oneKey);
			if (myMatch.find()) {
				String trimmedKey = oneKey.trim();
				String[] wordAr = trimmedKey.split(" ");
				if (rakeParams.shouldStem()) {
					String[] stemmedWordAr = new String[wordAr.length];
					for (int k = 0; k < wordAr.length; k++) {
						stemmedWordAr[k] = stemmer.stem(wordAr[k]).toString();
					}
					String stemedString = collapseTokens(stemmedWordAr);
					Keyword someKey = new Keyword(trimmedKey, wordAr, stemedString, stemmedWordAr);
					keywords.add(someKey);
				} else {
					Keyword someKey = new Keyword(trimmedKey, wordAr);
					keywords.add(someKey);
				}
			}
		}
		
		return keywords;
	}
	
	private String collapseTokens(String[] tokens) {
		
		StringBuilder fullBuff = new StringBuilder();
		
		for (int i = 0; i < tokens.length; i++) {
			String atok = tokens[i];	
			String toAdd = atok + " ";
			fullBuff.append(toAdd);
		}
	
		return fullBuff.toString();
	}
	
	private ArrayList<Keyword> calcKeywordScores(ArrayList<Keyword> candidateKeywords, RakeParams rakeParams) {
		 
		 Map<String, Integer> wordfreq = new HashMap<String, Integer>();
		 Map<String, Integer> worddegTemp = new HashMap<String, Integer>();
		 Map<String, Float> tokenScores = new HashMap<String, Float>();
		 
		 for (int i = 0; i < candidateKeywords.size(); i++) {
			 
			 Keyword oneKey = candidateKeywords.get(i);
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
			 tokenScores.put(aKey, val);
		}
		 
		 for (int i = 0; i < candidateKeywords.size(); i++) {
			 Keyword oneKey = candidateKeywords.get(i);
			 oneKey.sumScore(tokenScores, rakeParams);
		 }
		 
		 return candidateKeywords;
	}
	
	public Result getResult(ArrayList<Keyword> keywords) {
		
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