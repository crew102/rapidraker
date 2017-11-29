package org.crew102.rapidrake.model;

import java.io.FileInputStream;
import java.io.InputStream;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

// A Tagger is a wrapper around POSTaggerME

public class Tagger {
	
	private String inputString;
	
	public Tagger(String inputString) {
		this.inputString = inputString;
	}
	
	public POSTaggerME getPosTagger() throws java.io.IOException {
		InputStream modelIn = null;
		POSModel modelIn2 = null;
		try {
			modelIn = new FileInputStream(inputString);
			modelIn2 = new POSModel(modelIn);
		} catch(java.io.IOException ex) {
			throw new java.io.IOException("Couldn't find POS model based on URL", ex);
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch(java.io.IOException ex2) {
					throw new java.io.IOException(ex2);
				}
			}
		}
		return new POSTaggerME(modelIn2);
	}
		
}