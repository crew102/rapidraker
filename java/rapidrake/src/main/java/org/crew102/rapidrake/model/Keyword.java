package org.crew102.rapidrake.model;

import opennlp.tools.stemmer.snowball.SnowballStemmer;

// A Keyword is any n-gram that doesn't contain stop words/phrase delims

public class Keyword {
	
	private String keyString;
	private String[] keyStringAry;

	private String keyStemmedString;
	private String[] keyStemmedStringAry;
	
	private float score;
	private int freq; 

	public String[] getKeyStringAry() {
		return keyStringAry;
	}
	
	public Keyword(String keyString, String[] keyStringAry) {
		this.keyString = keyString;
		this.keyStringAry = keyStringAry;
		this.keyStemmedStringAry = new String[keyStringAry.length];
		this.keyStemmedString = new String();
	}
	
	public void stem(SnowballStemmer stemmer) {
		
		 for (int i = 0; i < keyStringAry.length; i++) {
			 String one = keyStringAry[i];
			 CharSequence k = stemmer.stem(one);
			 String stemmedToken = k.toString();
			 keyStemmedStringAry[i] = stemmedToken;
		 }
	}
	
}