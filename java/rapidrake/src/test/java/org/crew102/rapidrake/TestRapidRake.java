/*

package org.crew102.rapidrake;

import static org.junit.Assert.*;
import org.crew102.rapidrake.model.*;
import org.junit.Before;
import org.junit.Test;

public class TestRapidRake {
	
	RakeAlgorithm myAlg;
	Document myDoc;
	
	@Before
	public void setUp() throws java.io.IOException {
		 
			String[] stopWords = {"i", "do", "be"};
			String[] stopPOS = {"VB", "VBD", "VBG", "VBN", "VBP", "VBZ"};
			int wordMinChar = 2;
			boolean stem = true;
			RakeParams parms = new RakeParams(stopWords, stopPOS, wordMinChar, stem);
					
			myAlg = new RakeAlgorithm(parms, "model-bin/en-pos-maxent.bin");
			myDoc = new Document("this is some text. that has some keywords");
	}
	
    @Test
    public void tesInitTokens() {
    	Result myRes = myAlg.rake(myDoc);
    	float[] someScores = myRes.getScores();
    	double actual = (double) someScores[0];
    	
    	double expected = (double) 1.6;
    	
    	assertEquals(expected, actual, .1);
     }
   

}

*/