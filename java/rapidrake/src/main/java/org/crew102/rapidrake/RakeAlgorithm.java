package org.crew102.rapidrake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.stemmer.snowball.SnowballStemmer;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import org.crew102.rapidrake.model.*;

/**
 * The logic/implementation of the Rapid Automatic Keyword Extraction (RAKE) algorithm. The class's API includes:
 * 
 * <ul>
 * <li> A constructor which initializes the algorithm's parameters (stored in a {@link RakeParams} object), a POS 
 * 		tagger, and a sentence detector
 * <li> The {@link rake} method, which runs RAKE on a string
 * <li> The {@link getResult} method, which takes an array of {@link Keyword} objects and converts their relevant 
 * 		instance variables to primitive arrays (i.e., to a {@link Result}). This allows the results to be easily pulled 
 * 		out on the R side. 
 * </ul> 
 * 
 * @author Chris Baker
 */
public class RakeAlgorithm {
	
	private static RakeParams rakeParams;
	private static POSTaggerME tagger;
	private static SentenceDetectorME sentDetector;
	private static final SnowballStemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
	
    /**
     * Constructor.
     *
     * @param rakeParams the parameters that RAKE will use
     * @param taggerModelUrl the URL of the trained POS tagging model
     * @param sentDectModelUrl the URL of the trained sentence detection model
     * @see RakeParams
     */
	public RakeAlgorithm(RakeParams rakeParams, String taggerModelUrl, String sentDectModelUrl) throws java.io.IOException {
		RakeAlgorithm.rakeParams = rakeParams;
		RakeAlgorithm.tagger = new Tagger(taggerModelUrl).getPosTagger();
		RakeAlgorithm.sentDetector = new SentDetector(sentDectModelUrl).getSentDetector();
	}
	
    /**
     * Run RAKE on a single string.
     *
     * @param txtEl a string with the text that you want to run RAKE on
     * @return a data object containing the results of RAKE (the keywords in their full form and stemmed form, as 
     * 		   well as their scores)
     * @see Result
     */
	public Result rake(String txtEl) {
		String[] tokens = getTokens(txtEl);
		ArrayList<Keyword> keywords = idCandidateKeywords(tokens);
		ArrayList<Keyword> keywords2 = calcKeywordScores(keywords);		
		return getResult(keywords2);
	}
	
	private String[] getTokens(String txtEl) {
		
		// Have to pad punctuation chars with spaces so that tokenizer doesn't combine words with punctuation chars
		String txtPadded = txtEl.replaceAll("([-,.?():;\"!/])", " $1 ");
		
		ArrayList<String> tokenList = new ArrayList<String>();
		Pattern anyWordChar = Pattern.compile("[a-z]");
		
		String[] sents = sentDetector.sentDetect(txtPadded);
				
		for (String sentence : sents) {
			
			String[] tokenArray = WhitespaceTokenizer.INSTANCE.tokenize(sentence);
			String[] tags = tagger.tag(tokenArray);
			
			for (int i = 0; i < tokenArray.length; i++) {
				
				String token = tokenArray[i].trim().toLowerCase();
				String tag = tags[i].trim();
				
				// replace unwanted tokens with a period, which we can be confident will be used as a delimiter
				if (rakeParams.getStopPOS().contains(tag) || token.length() < rakeParams.getWordMinChar() || 
						rakeParams.getStopWords().contains(token) || !anyWordChar.matcher(token).find()) {
					token = ".";
				}
				
				tokenList.add(token);
			}
		}
		
		String[] tokens = new String[tokenList.size()];
		return tokenList.toArray(tokens);
	}

	private ArrayList<Keyword> idCandidateKeywords(String[] tokens) {
		
		ArrayList<Keyword> keywords = new ArrayList<Keyword>();
		String cleanedTxt = collapseTokens(tokens);
		String[] aryKey = cleanedTxt.split(rakeParams.getPhraseDelmins());
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
			String toAdd;
			if (i != tokens.length - 1) {
				toAdd = atok + " ";
			} else {
				toAdd = atok;
			}
			fullBuff.append(toAdd);
		}
	
		return fullBuff.toString();
	}
	
	private ArrayList<Keyword> calcKeywordScores(ArrayList<Keyword> candidateKeywords) {
		 
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
	
	/**
	 * Convert a list of keywords to a {@link Result}.
	 * 
	 * @param keywords a list of extracted keywords
	 * @return A data object containing the results of RAKE
     * @see Keyword
     * @see Result
	 */
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