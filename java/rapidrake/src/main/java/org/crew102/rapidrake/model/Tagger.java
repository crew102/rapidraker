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
		InputStream modelIn2 = new FileInputStream(inputString);	
		POSModel model2 = new POSModel(modelIn2);
		return new POSTaggerME(model2);
	}
		
}