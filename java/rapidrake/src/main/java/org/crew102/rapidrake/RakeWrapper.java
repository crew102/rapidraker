package org.crew102.rapidrake;

import java.util.ArrayList;
import org.crew102.rapidrake.model.*;

// A RakeWrapper creates a RakeAlg obj and calls rake on Documents

public class RakeWrapper { 
	
	private RakeAlgorithm rakeAlg;
	private ArrayList<Document> documents;

	public RakeWrapper(String[] txt, String[] stopWords, String[] stopPOS, 
					int wordMinChar, boolean stem) throws java.io.IOException {
		
		 documents = new ArrayList<Document>();

		// Add documents
		for (int i = 0; i < txt.length; i++) {
			Document aDoc = new Document(txt[i]);
			documents.add(aDoc);
		}
				
		// Create a RakeAlg
		rakeAlg = new RakeAlgorithm(stopWords, stopPOS, wordMinChar, stem);
	}
	
	// For testing
	public RakeWrapper() throws java.io.IOException {
		
		 documents = new ArrayList<Document>();
		
		String[] txt = new String[] {"i love dogs, what do you think? can you dig it?", 
				"this is also some text."};
		
		for (int i = 0; i < txt.length; i++) {
			Document aDoc = new Document(txt[i]);
			documents.add(aDoc);
		}
		
		String[] stopWords = new String[] {"i", "do", "be"};
		String[] stopPOS = new String[] {"VB", "VBD", "VBG", "VBN", "VBP", "VBZ"};
		int wordMinChar = 2;
		boolean stem = true;
		rakeAlg = new RakeAlgorithm(stopWords, stopPOS, wordMinChar, stem);
	}
	
	public String[] run() throws java.io.IOException {
		
		rakeAlg.loadAllFuns();
		
		for (int i = 0; i < documents.size(); i++) {
			rakeAlg.rake(documents.get(i));
		}
		
		// For testing r/java api...Return type needs to actually be Keyword[]
		String[] testRes = new String[] {"hi there", "old friend"};
		return testRes;
	}
	
	// For testing
	public static void main(String[] args) throws java.io.IOException {
		RakeWrapper wrapper = new RakeWrapper();
		wrapper.run();
	}
	
}