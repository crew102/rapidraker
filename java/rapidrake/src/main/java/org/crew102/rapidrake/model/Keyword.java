package org.crew102.rapidrake.model;

import java.util.Map;

// A Keyword is any n-gram that doesn't contain stop words/phrase delims

public class Keyword {
		
	private String keyString; // e.g., "good dogs"
	private String keyStemmedString; // e.g., "good dog"
	
	private String[] keyStringAry; // e.g., {"good", "dogs"}
	private String[] keyStemmedStringAry; // e.g., {"good", "dog"}

	private float score;
	
	public String[] getKeyStringAry() {
		return keyStringAry;
	}
	public String[] getKeyStemmedAry() {
		return keyStemmedStringAry;
	}
	
	/// 
	public String getKeyString() {
		return keyString;
	}
	public String getStemmedString() {
		return keyStemmedString;
	}
	public float getScore() {
		return score;
	}
	
	public Keyword(String keyString, String[] keyStringAry) {
		this.keyString = keyString;
		this.keyStringAry = keyStringAry;
		this.keyStemmedStringAry = new String[keyStringAry.length];
		this.keyStemmedString = new String();
	}
	
	public void sumScore(Map<String, Float> scoreVec, RakeParams rakeParams) {
		
		String[] ary;
		if (rakeParams.shouldStem()) {
			ary = keyStemmedStringAry;
		} else {
			ary = keyStringAry;
		}
		
		float sum = 0;
		for (int i = 0; i < ary.length; i++) {
			String oneToke = ary[i];
			float val = scoreVec.get(oneToke);
			sum = val + sum;
		}
		score = sum;
	}
	
}